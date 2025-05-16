package be.vdab.scrumjava202504.orders;

import java.util.List;

public interface OrderRepository {
    long getOrdersCount();

    List<DisplayOrder> getDisplayOrders();

    List<OrderDetails> getOrderDetailsByOrderId(long orderId);
}
