package be.vdab.scrumjava202504.Orders;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

