package be.vdab.scrumjava202504.orders;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
  
    @GetMapping("/count")
    public long getOrdersCount() {
        return orderService.getOrdersCount();
    }

    /**
     *
     * @return a list of display orders with their ID, number of products, and total weight.
     * max of 5 orders
     */
    @GetMapping("/display")
    public List<DisplayOrder> getDisplayOrders() {
        return orderService.getDisplayOrders();
    }

    /**
     * Retrieves an optimized picking route for the given order ID.
     *
     * @param id The unique identifier of the order for which the picking route is requested.
     * @return A list of {@link PickingItem} objects representing the optimized picking sequence.
     */
    @GetMapping("/getOrderRoute/{id}")
    public List<PickingItem> getOrderRoute(@PathVariable @PositiveOrZero long id) {
        return orderService.getOrderDetailsByOrderId(id);
    }

    @PostMapping("/finishOrder/{id}")
    public void postFinishOrder(@PathVariable @PositiveOrZero long id) {
        orderService.finishOrder(id);
    }
}

