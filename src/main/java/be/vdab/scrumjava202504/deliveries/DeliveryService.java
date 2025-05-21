package be.vdab.scrumjava202504.deliveries;

import be.vdab.scrumjava202504.products.SuppliersRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final SuppliersRepository suppliersRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, SuppliersRepository suppliersRepository) {
        this.deliveryRepository = deliveryRepository;
        this.suppliersRepository = suppliersRepository;
    }

    public long createDelivery(NewDelivery newDelivery) {
        var supplierId = suppliersRepository.fetchSupplierIdByName(newDelivery.getSupplierName())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        var delivery = new Delivery(0, supplierId, newDelivery.getDeliveryTicketNumber(),
                newDelivery.getDeliveryTicketDate(), newDelivery.getDeliveryDate());

        return deliveryRepository.create(delivery);
    }
}
