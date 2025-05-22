package be.vdab.scrumjava202504.deliveries;

import be.vdab.scrumjava202504.exception.ProductNotFoundException;
import be.vdab.scrumjava202504.products.Product;
import be.vdab.scrumjava202504.products.ProductDTO;
import be.vdab.scrumjava202504.products.ProductRepository;
import be.vdab.scrumjava202504.products.SuppliersRepository;
import be.vdab.scrumjava202504.warehouseLocations.WarehouseLocationRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final SuppliersRepository suppliersRepository;
    private final ProductRepository productRepository;
    private final WarehouseLocationRepository warehouseLocationRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, SuppliersRepository suppliersRepository, ProductRepository productRepository, WarehouseLocationRepository warehouseLocationRepository) {
        this.deliveryRepository = deliveryRepository;
        this.suppliersRepository = suppliersRepository;
        this.productRepository = productRepository;
        this.warehouseLocationRepository = warehouseLocationRepository;
    }

    /**
     * Creates a new delivery in the database.
     *
     * @param newDelivery the new delivery to be created
     * @return the ID of the created delivery
     */
    public long createDelivery(NewDelivery newDelivery) {
        var supplierId = suppliersRepository.fetchSupplierIdByName(newDelivery.getSupplierName())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        var delivery = new Delivery(0, supplierId, newDelivery.getDeliveryTicketNumber(),
                newDelivery.getDeliveryTicketDate(), newDelivery.getDeliveryDate());

        return deliveryRepository.create(delivery);
    }

    /**
     * Converts an EAN to an article ID, builds a NewDeliveryLineWithId object,
     * and stores it in the database.
     *
     * @param newDeliveryLine The new delivery line including the EAN
     * @throws IllegalArgumentException if the EAN does not exist
     */
    public void createDeliveryLine(NewDeliveryLine newDeliveryLine) {
        var article = productRepository.findProductByEanNumber(newDeliveryLine.getEan())
                .orElseThrow(() -> new IllegalArgumentException("No article found for EAN: " + newDeliveryLine.getEan()));

        newDeliveryLine.setArticleId(article.getProductId());

        deliveryRepository.create(newDeliveryLine);
    }


    /**
     * Calculates an efficient placement plan for a set of delivered products.
     * <p>
     * The method prioritizes placing products in existing locations where the product is already stored,
     * and then uses empty locations if necessary. It ensures that only one type of product is placed
     * per location and respects the maximum allowed quantity per location.
     * <p>
     * The final placement plan is sorted by shelf and position to optimize the walking route.
     *
     * @param deliveredProducts a map of product IDs to the quantity delivered
     * @return a sorted list of {@link PlacementItem} objects representing the placement instructions
     * @throws IllegalStateException if there is not enough space in the warehouse for a product
     */

    public List<PlacementItem> calculateMostEfficientPlacingRoute(Map<Long, Integer> deliveredProducts) {
        List<PlacementItem> placementPlan = new ArrayList<>();
        List<ProductDTO> allLocations = warehouseLocationRepository.findAllLocationsSorted();

        // Remember which locations are used
        Set<String> usedLocations = new HashSet<>();

        for (Map.Entry<Long, Integer> entry : deliveredProducts.entrySet()) {
            long productId = entry.getKey();
            int remaining = entry.getValue();

            Product product = productRepository.findAndLockByArtikelId(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));
            int maxPerLocation = product.getMaxInStockPlace().intValue();

            List<ProductDTO> existingLocations = allLocations.stream()
                    .filter(loc -> loc.getProductId() == productId)
                    .toList();

            List<ProductDTO> emptyLocations = allLocations.stream()
                    .filter(loc -> loc.getProductId() == 0 && loc.getQuantity() == 0)
                    .toList();

            List<ProductDTO> candidates = new ArrayList<>();
            candidates.addAll(existingLocations);
            candidates.addAll(emptyLocations);

            for (ProductDTO location : candidates) {
                if (remaining <= 0) break;

                String locationKey = location.getShelf() + "-" + location.getPosition();
                if (usedLocations.contains(locationKey)) continue;

                int currentQuantity = location.getQuantity();
                int availableSpace = maxPerLocation - currentQuantity;

                if (availableSpace > 0) {
                    int toPlace = Math.min(availableSpace, remaining);

                    placementPlan.add(new PlacementItem(
                            location.getShelf(),
                            location.getPosition(),
                            product.getName(),
                            toPlace,
                            productId
                    ));

                    usedLocations.add(locationKey);
                    remaining -= toPlace;
                }
            }

            if (remaining > 0) {
                throw new IllegalStateException("Not enough space in the warehouse for productId: " + productId);
            }
        }

        return placementPlan.stream()
                .sorted(Comparator.comparing(PlacementItem::getShelf)
                        .thenComparingInt(PlacementItem::getPosition))
                .collect(Collectors.toList());
    }



}
