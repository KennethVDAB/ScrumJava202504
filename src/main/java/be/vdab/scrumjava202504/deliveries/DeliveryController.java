package be.vdab.scrumjava202504.deliveries;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }
    /**
     * Creates a new delivery in the database.
     *
     * @param newDelivery the new delivery to be created
     * @return the ID of the created delivery
     */
    @PostMapping("create")
    public long createDelivery(@RequestBody NewDelivery newDelivery) {
        return deliveryService.createDelivery(newDelivery);
    }

    /**
     * Creates a new deliveryLine in the database.
     *
     * @param newDeliveryLine the new delivery line to be created
     */
    @PostMapping("lines")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDeliveryLine(@RequestBody @Valid NewDeliveryLine newDeliveryLine) {
        deliveryService.createDeliveryLine(newDeliveryLine);
    }
}
