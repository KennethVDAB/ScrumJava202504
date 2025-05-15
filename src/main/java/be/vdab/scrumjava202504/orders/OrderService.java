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
        // Haal per order de producten en hun hoeveelheid op
        Map<Long, BigDecimal> orderedProductsWithQuantity = orderRepository.getOrderDetailsByOrderId(orderId)
                .stream()
                .collect(Collectors.toMap(OrderDetails::getProductId, OrderDetails::getQuantityOrder));

        List<PickingItem> result = new ArrayList<>();

        // Voor elk besteld product werken we de picking-instructies uit op basis van de meest efficiënte route.
        orderedProductsWithQuantity.forEach((productId, quantityBigDecimal) -> {
            int quantityAsked = quantityBigDecimal.intValue();

            // Haal alle mogelijke locaties voor dit product op
            List<ProductDTO> candidateLocations = productRepository.findByArtikelId(productId);

            // Bereken de picking items voor dit product met de nieuwe route cost methode
            List<PickingItem> pickingItems = getEfficientPickingItemsForProduct(productId, quantityAsked, candidateLocations);

            result.addAll(pickingItems);
        });

        // Sorteer de uiteindelijke picking lijst op rek en positie zodat de plukker een overzichtelijke volgorde krijgt.
        return result.stream()
                .sorted(Comparator.comparing(PickingItem::getShelf)
                        .thenComparingInt(PickingItem::getPosition))
                .collect(Collectors.toList());
    }

    /**
     * Berekent voor een gegeven product de picking items op basis van de beschikbaarheid én
     * de route kost (gebaseerd op het aantal stappen). Eerst worden alle locaties gesorteerd op
     * de berekende round-trip kosten. Vervolgens wordt cumulatief gecheckt of de verzamelde hoeveelheid
     * aan de optimale locaties het gewenste aantal bereikt.
     *
     * @param productId         Het product-ID.
     * @param quantityAsked     De gevraagde hoeveelheid voor dit product.
     * @param candidateLocations  De lijst met beschikbare locaties voor dit product.
     * @return Een lijst met PickingItem-objecten voor de order.
     */
    private List<PickingItem> getEfficientPickingItemsForProduct(long productId, int quantityAsked, List<ProductDTO> candidateLocations) {
        // Sorteer eerst op basis van de round-trip route cost.
        List<ProductDTO> sortedLocations = candidateLocations.stream()
                .sorted(Comparator.comparingInt(this::calculateRouteCost))
                .collect(Collectors.toList());

        int remaining = quantityAsked;

        List<PickingItem> pickedItems = new ArrayList<>();

        // Ga de locaties af van de laagste naar de hoogste route kost totdat de gevraagde hoeveelheid is bereikt.
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
                        productId
                ));
                remaining -= qtyToPick;
            }
        }

        // Eventueel kun je hier een controle toevoegen om te signaleren dat niet aan de gevraagde hoeveelheid is voldaan.
        return pickedItems;
    }

    /**
     * Bereken de round-trip route cost voor een locatie.
     *
     * @param location De ProductDTO-locatie.
     * @return De berekende route cost (in stappen).
     */
    private int calculateRouteCost(ProductDTO location) {
        // Haal de cijferwaarde op van de rek-letter via LetterToNumber
        int shelfFactor = LetterToNumber.getNumberOfChar(location.getShelf().charAt(0));

        // Bereken de rek-kost: stel dat elk rek-niveau 10 stappen extra vereist
        int shelfCost = shelfFactor * 10;

        // Bereken de positie-kost (heen en terug)
        int positionCost = 2 * location.getPosition();

        return shelfCost + positionCost;
    }
}