package be.vdab.scrumjava202504.Orders;

import java.util.List;

public interface OrderRepository {
    long getOrdersCount();

    List<DisplayOrder> getDisplayOrders();
}
