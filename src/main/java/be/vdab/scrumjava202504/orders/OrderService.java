package be.vdab.scrumjava202504.orders;

import be.vdab.scrumjava202504.products.ProductDTO;
import be.vdab.scrumjava202504.products.ProductRepository;
import be.vdab.scrumjava202504.util.LetterToNumber;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
  
    public long getOrdersCount() {
        return orderRepository.getOrdersCount();
    }

    public List<DisplayOrder> getDisplayOrders() {
        return orderRepository.getDisplayOrders();
    }

    public List<PickingItem> getOrderDetailsByOrderId(long orderId) {
        Map<Long, BigDecimal> orderedProductsWithQuantity = orderRepository.getOrderDetailsByOrderId(orderId)
                .stream()
                .collect(Collectors.toMap(OrderDetails::getProductId, OrderDetails::getQuantityOrder));

        List<PickingItem> notOrderedPickingRoute = new ArrayList<>();

        for (Map.Entry<Long, BigDecimal> entry : orderedProductsWithQuantity.entrySet()) {
            int productId = entry.getKey().intValue();
            int quantityAsked = entry.getValue().intValue();

            List<ProductDTO> allPossibleProductLocations = productRepository.findByArtikelByIdAndQuantity(productId, quantityAsked);

            ProductDTO bestLocation = allPossibleProductLocations.stream()
                    .min(Comparator.comparingInt(product -> calculateContextualStepValue(product, orderedProductsWithQuantity)))
                    .orElse(null);

            if (bestLocation != null) {
                PickingItem item = new PickingItem(
                        bestLocation.getShelf(),
                        bestLocation.getPosition(),
                        bestLocation.getName(),
                        quantityAsked,
                        productId
                );
                notOrderedPickingRoute.add(item);
            }
        }

        List<PickingItem> sortedProducts = notOrderedPickingRoute.stream()
                .sorted(Comparator.comparingInt(p -> LetterToNumber.getNumberOfChar(p.getShelf().charAt(0)) + p.getPosition()))
                .collect(Collectors.toList());

        return sortedProducts;
    }

    private int calculateContextualStepValue(ProductDTO toEvaluateLocation, Map<Long, BigDecimal> allProductsInOrderWithQuantity) {
        int shelfValue = LetterToNumber.getNumberOfChar(toEvaluateLocation.getShelf().charAt(0));
        int positionValue = toEvaluateLocation.getPosition();

        List<ProductDTO> relevantLocations = new ArrayList<>();

        for (Map.Entry<Long, BigDecimal> entry : allProductsInOrderWithQuantity.entrySet()) {
            Long productId = entry.getKey();
            if (productId != toEvaluateLocation.getProductId()) {
                relevantLocations.addAll(productRepository.findByArtikelByIdAndQuantity(productId.intValue(), entry.getValue().intValue()));
            }
        }

        int totalDistance = relevantLocations.stream()
                .mapToInt(p -> Math.abs(shelfValue - LetterToNumber.getNumberOfChar(p.getShelf().charAt(0)))
                        + Math.abs(positionValue - p.getPosition()))
                .sum();

        return totalDistance;
    }
}

