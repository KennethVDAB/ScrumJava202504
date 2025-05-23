package be.vdab.scrumjava202504.orders;

import be.vdab.scrumjava202504.exception.OrderNotFoundException;
import be.vdab.scrumjava202504.products.ProductDTO;
import be.vdab.scrumjava202504.products.ProductRepository;
import be.vdab.scrumjava202504.util.LetterToNumber;
import be.vdab.scrumjava202504.warehouseLocations.LocationNotFoundException;
import be.vdab.scrumjava202504.warehouseLocations.WarehouseLocationRepository;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@code OrderService} class provides methods to retrieve order details and to generate
 * picking instructions for orders based on product availability and route optimization.
 * <p>
 * The service calculates an efficient route cost for each product location by combining a shelf cost
 * (derived from converting the shelf letter into a numeric value via {@code LetterToNumber}) and a position cost
 * (representing the steps required to travel to and from the location).
 * <p>
 * The route cost is computed with the following formula:
 * <pre>
 *    routeCost = (LetterToNumber.getNumberOfChar(shelfLetter) * 10) + (2 * position)
 * </pre>
 * This allows the system to sort product locations and pick items in the most efficient sequence.
 */
@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final WarehouseLocationRepository warehouseLocationRepository;

    /**
     * Constructs a new {@code OrderService} with the specified order and product repositories.
     *
     * @param orderRepository   the repository used to retrieve order details
     * @param productRepository the repository used to retrieve product details and locations
     */
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, WarehouseLocationRepository warehouseLocationRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.warehouseLocationRepository = warehouseLocationRepository;
    }

    /**
     * Retrieves the total number of orders.
     *
     * @return the count of orders as a {@code long} value
     */
    public long getOrdersCount() {
        return orderRepository.getOrdersCount();
    }

    /**
     * Retrieves a list of orders for display purposes.
     *
     * @return a list of {@code DisplayOrder} objects
     */
    public List<DisplayOrder> getDisplayOrders() {
        return orderRepository.getDisplayOrders();
    }

    /**
     * Retrieves detailed picking instructions for a specified order.
     * <p>
     * For each ordered product, this method retrieves the available locations,
     * calculates the optimal picking instructions based on route cost (including both shelf and position factors),
     * and returns an organized list of {@code PickingItem} objects sorted by shelf and position.
     *
     * @return a list of {@code PickingItem} objects, each representing a product location and the quantity to pick
     */
    public List<PickingItem> getOrderDetailsByOrderId() {

        DisplayOrder displayOrder = this.orderRepository.getDisplayOrders().getFirst();

        // Retrieve the products and their ordered quantities for the given order.
        Map<Long, BigDecimal> orderedProductsWithQuantity = orderRepository.getOrderDetailsByOrderId(displayOrder.getId())
                .stream()
                .collect(Collectors.toMap(OrderDetails::getProductId, OrderDetails::getQuantityOrder));

        List<PickingItem> result = new ArrayList<>();

        // For each ordered product, generate picking instructions based on the most efficient route.
        orderedProductsWithQuantity.forEach((productId, quantityBigDecimal) -> {
            int quantityAsked = quantityBigDecimal.intValue();

            // Retrieve all possible locations for this product.
            List<ProductDTO> candidateLocations = productRepository.findByArtikelId(productId);

            // Calculate the picking items for this product using the new route cost method.
            List<PickingItem> pickingItems = getEfficientPickingItemsForProduct(productId, quantityAsked, candidateLocations, displayOrder.getId());

            result.addAll(pickingItems);
        });

        // Sort the final picking list by shelf and then by position to provide the picker with an organized route.
        return result.stream()
                .sorted(Comparator.comparing(PickingItem::getShelf)
                        .thenComparingInt(PickingItem::getPosition))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the picking items for a specific product based on availability and route cost optimization.
     * <p>
     * This method sorts the candidate locations by their computed round-trip route cost,
     * then iterates through the sorted list and cumulatively selects quantities from each location until
     * the requested quantity is met.
     *
     * @param productId          the product identifier
     * @param quantityAsked      the requested quantity for the product
     * @param candidateLocations the list of available product locations
     * @return a list of {@code PickingItem} objects representing the selected product locations and the picked quantity
     */
    private List<PickingItem> getEfficientPickingItemsForProduct(long productId, int quantityAsked, List<ProductDTO> candidateLocations, long orderId) {
        // First, sort the locations based on the computed round-trip route cost.
        List<ProductDTO> sortedLocations = candidateLocations.stream()
                .sorted(Comparator.comparingInt(this::calculateRouteCost))
                .collect(Collectors.toList());

        int remaining = quantityAsked;
        List<PickingItem> pickedItems = new ArrayList<>();

        // Iterate through the sorted locations, starting with the location with the lowest route cost,
        // until the requested quantity is reached.
        for (ProductDTO candidate : sortedLocations) {
            if (remaining <= 0) {
                break;
            }

            int available = candidate.getQuantity();

            if (available > 0) {
                int qtyToPick = Math.min(available, remaining);

                pickedItems.add(new PickingItem(
                        candidate.getShelf(),
                        candidate.getPosition(),
                        candidate.getName(),
                        qtyToPick,
                        productId,
                        orderId

                ));
                remaining -= qtyToPick;
            }
        }

        // Optionally, you can add error handling here if the available stock is insufficient.
        return pickedItems;
    }

    /**
     * Calculates the round-trip route cost for a given product location.
     * <p>
     * The route cost is calculated by combining:
     * <ul>
     *   <li>a shelf cost, derived by converting the shelf letter to a numeric value using {@code LetterToNumber}
     *       and multiplying by 10, and</li>
     *   <li>a position cost, which is computed as 2 times the position value (representing the go-and-return trip).</li>
     * </ul>
     * For example, for a location "A40" (where A translates to 1), the calculation would be:
     * <pre>
     *    shelfCost = 1 * 10 = 10
     *    positionCost = 2 * 40 = 80
     *    routeCost = 10 + 80 = 90
     * </pre>
     *
     * @param location the {@code ProductDTO} object representing the product location
     * @return the computed route cost (in steps) as an {@code int}
     */
    int calculateRouteCost(ProductDTO location) {
        // Retrieve the numeric value of the shelf letter using LetterToNumber.
        int shelfFactor = LetterToNumber.getNumberOfChar(location.getShelf().charAt(0));

        // Calculate the position cost (steps to and from the location).
        int positionCost = 2 * location.getPosition();

        return shelfFactor + positionCost;
    }

    /**
     * Completes an order by updating stock levels and changing the order status.
     *
     * <p>
     * This method retrieves an order by its ID, locks it, and processes the associated items.
     * For each item:
     * <ul>
     *   <li>The ordered quantity is deducted from the product's stock.</li>
     *   <li>The warehouse inventory levels are updated accordingly.</li>
     * </ul>
     * </p>
     *
     * <p>
     * If the order is not found, an {@link OrderNotFoundException} is thrown.
     * If the warehouse location is not found, a {@link LocationNotFoundException} is thrown.
     * </p>
     *
     * @param orderId The unique ID of the order.
     * @throws OrderNotFoundException If the order is not found.
     * @throws LocationNotFoundException If the warehouse location is not found.
     */
    @Transactional(readOnly = false)
    public void finishOrder(@PositiveOrZero long orderId) {
        this.orderRepository.findAndLockById(orderId).ifPresentOrElse(order -> {

            this.getOrderDetailsByOrderId().forEach(pickingItem -> {
                BigDecimal quantityOrdered = BigDecimal.valueOf(pickingItem.getPickedQuantity());

                this.productRepository.updateStock(pickingItem.getProductId(), quantityOrdered);

                this.warehouseLocationRepository.findBySelfAndPositionAndLockById(pickingItem.getShelf(), pickingItem.getPosition())
                        .ifPresentOrElse(warehouseLocation -> {
                            BigDecimal newAmount = warehouseLocation.getAmount().subtract(quantityOrdered);
                            this.warehouseLocationRepository.updateAmount(pickingItem.getShelf(), pickingItem.getPosition(), newAmount);
                        }, () -> { throw new LocationNotFoundException(pickingItem.getShelf(), pickingItem.getPosition()); });
            });

            this.orderRepository.updateOrderStatus(orderId, 5);

        }, () -> { throw new OrderNotFoundException(orderId); });
    }
}
