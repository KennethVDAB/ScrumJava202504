package be.vdab.scrumjava202504.deliveries;

import be.vdab.scrumjava202504.products.ProductRepository;
import be.vdab.scrumjava202504.products.SuppliersRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final SuppliersRepository suppliersRepository;
    private final ProductRepository productRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, SuppliersRepository suppliersRepository, ProductRepository productRepository) {
        this.deliveryRepository = deliveryRepository;
        this.suppliersRepository = suppliersRepository;
        this.productRepository = productRepository;
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
}
