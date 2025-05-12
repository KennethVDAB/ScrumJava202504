package be.vdab.scrumjava202504.Orders;


import org.springframework.stereotype.Service;

@Service

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
