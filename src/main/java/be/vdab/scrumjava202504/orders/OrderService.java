package be.vdab.scrumjava202504.orders;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
  
    public long getOrdersCount() {
        return orderRepository.getOrdersCount();
    }

    public List<DisplayOrder> getDisplayOrders() {
        return orderRepository.getDisplayOrders();
    }

    public List<PickingItem> getOrderDetailsByOrderId(long orderId) {
        throw new UnsupportedOperationException();
    }
}

