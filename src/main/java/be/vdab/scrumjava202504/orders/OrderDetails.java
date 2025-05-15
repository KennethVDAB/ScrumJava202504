package be.vdab.scrumjava202504.orders;

import java.math.BigDecimal;

public class OrderDetails {
    private long orderDetailId;
    private long orderId;
    private long productId;
    private BigDecimal quantityOrder;

    public OrderDetails(long orderDetailId, long orderId, long productId, BigDecimal quantityOrder) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantityOrder = quantityOrder;
    }

    public long getOrderDetailId() {
        return orderDetailId;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
        return productId;
    }

    public BigDecimal getQuantityOrder() {
        return quantityOrder;
    }
}
