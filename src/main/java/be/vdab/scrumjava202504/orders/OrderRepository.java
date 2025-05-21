package be.vdab.scrumjava202504.orders;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    long getOrdersCount();

    List<DisplayOrder> getDisplayOrders();

    List<OrderDetails> getOrderDetailsByOrderId(long orderId);

    Optional<Order> findAndLockById(long orderId);

    void updateOrderStatus(long orderId, long orderStatusId);

}
