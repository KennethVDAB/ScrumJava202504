package be.vdab.scrumjava202504.orders;

import be.vdab.scrumjava202504.products.ProductDTO;
import be.vdab.scrumjava202504.products.ProductRepository;
import be.vdab.scrumjava202504.util.LetterToNumber;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
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
        // Haal de ordered producten en hun hoeveelheden op
        Map<Long, BigDecimal> orderedProductsWithQuantity = orderRepository.getOrderDetailsByOrderId(orderId)
                .stream()
                .collect(Collectors.toMap(OrderDetails::getProductId, OrderDetails::getQuantityOrder));

        List<PickingItem> result = new ArrayList<>();

        for (Map.Entry<Long, BigDecimal> orderedProductWithQuantity : orderedProductsWithQuantity.entrySet()) {
            long productId = orderedProductWithQuantity.getKey();
            int quantityAsked = orderedProductWithQuantity.getValue().intValue();

            // Alle beschikbare locaties ophalen voor dit product
            List<ProductDTO> allPossibleProductLocations = productRepository.findByArtikelId(productId);

            // Eerst: Als een enkele locatie de volledige hoeveelheid kan leveren.
            List<ProductDTO> sufficientLocations = allPossibleProductLocations.stream()
                    .filter(location -> location.getQuantity() >= quantityAsked)
                    .toList();

            if (!sufficientLocations.isEmpty()) {
                // Kies de locatie met de beste (laagste) contextual step value
                ProductDTO bestLocation = sufficientLocations.stream()
                        .min(Comparator.comparingInt(product -> calculateContextualStepValue(product, orderedProductsWithQuantity)))
                        .orElse(null);

                if (bestLocation != null) {
                    result.add(new PickingItem(
                            bestLocation.getShelf(),
                            bestLocation.getPosition(),
                            bestLocation.getName(),
                            quantityAsked,
                            productId
                    ));
                }
            } else {
                // Geen enkele locatie heeft op zich genoeg voorraad:
                // Groepeer de beschikbare locaties per rek:
                Map<String, List<ProductDTO>> locationsByShelf = allPossibleProductLocations.stream()
                        .collect(Collectors.groupingBy(p -> p.getShelf()));

                // Kies per rek de "beste" locatie (bijvoorbeeld de locatie met de laagste positie)
                List<ProductDTO> candidates = locationsByShelf.entrySet().stream()
                        .map(entry -> entry.getValue()
                                .stream()
                                .min(Comparator.comparingInt(ProductDTO::getPosition))
                                .orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                // Sorteer de kandidaten op een logische routevolgorde.
                // In dit voorbeeld sorteren we eerst op rek (alfabetisch) en dan op positie.
                candidates.sort(Comparator.comparing(ProductDTO::getShelf)
                        .thenComparingInt(ProductDTO::getPosition));

                int remaining = quantityAsked;

                // Eerst proberen we per rek maximaal één keer te picken (dus 1 per groep)
                for (ProductDTO candidate : candidates) {
                    if (remaining <= 0) {
                        break;
                    }

                    int available = candidate.getQuantity();

                    if (available > 0) {
                        int qtyFromLocation = Math.min(available, 1);

                        result.add(new PickingItem(
                                candidate.getShelf(),
                                candidate.getPosition(),
                                candidate.getName(),
                                qtyFromLocation,
                                productId
                        ));

                        remaining -= qtyFromLocation;
                    }
                }

                // Mocht de eerste ronde per groep niet voldoende zijn, dan herhaal je de procedure
                // maar nu binnen elke groep waarbij algepickte kandidaten overgeslagen worden.
                if (remaining > 0) {
                    // Voor elke rek, bekijk of er meer locaties beschikbaar zijn (anders dan de reeds gekozen)
                    for (Map.Entry<String, List<ProductDTO>> entry : locationsByShelf.entrySet()) {
                        // Sorteer ook hier op positie
                        List<ProductDTO> sortedLocations = entry.getValue().stream()
                                .sorted(Comparator.comparingInt(ProductDTO::getPosition))
                                .toList();

                        // Kijk naar de overige, indien aanwezig
                        if (sortedLocations.size() > 1 && remaining > 0) {
                            // Kies de volgende in de volgorde (bijvoorbeeld de 2de locatie)
                            ProductDTO candidate = sortedLocations.get(1);

                            int available = candidate.getQuantity();

                            if (available > 0) {

                                int qty = Math.min(available, remaining);

                                result.add(new PickingItem(
                                        candidate.getShelf(),
                                        candidate.getPosition(),
                                        candidate.getName(),
                                        qty,
                                        productId
                                ));

                                remaining -= qty;
                            }
                        }

                        if (remaining <= 0) break;
                    }
                }
            }
        }

        // Sorteer de uiteindelijke picking lijst op shelf en positie
        List<PickingItem> sortedProducts = result.stream()
                .sorted(Comparator.comparing(PickingItem::getShelf)
                        .thenComparingInt(PickingItem::getPosition))
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
                relevantLocations.addAll(productRepository.findByArtikelId(productId));
            }
        }

        int totalDistance = relevantLocations.stream()
                .mapToInt(p -> Math.abs(shelfValue - LetterToNumber.getNumberOfChar(p.getShelf().charAt(0)))
                        + Math.abs(positionValue - p.getPosition()))
                .sum();

        return totalDistance;
    }
}

