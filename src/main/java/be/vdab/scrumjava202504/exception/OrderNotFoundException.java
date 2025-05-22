package be.vdab.scrumjava202504.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(long orderId) {
        super("Order with id " + orderId + " not found");
    }
}
