package be.vdab.scrumjava202504.Orders;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public long getOrdersCount() {
        return orderRepository.getOrdersCount();
    }
}
