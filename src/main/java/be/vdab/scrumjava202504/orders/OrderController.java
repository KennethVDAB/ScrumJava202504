package be.vdab.scrumjava202504.orders;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

