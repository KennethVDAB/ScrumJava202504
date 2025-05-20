package be.vdab.scrumjava202504.orders;

import java.time.LocalDateTime;

public class Order {
    private final long id;
    private final LocalDateTime orderDate;
    private final long customerId;
    private final boolean paid;
    private final String paymentCode;
    private final long paymentId;
    private long orderStatusId;
    private final boolean dealCodeUsed;
    private final String companyName;
    private final String BTWNumber;
    private final String firstName;
    private final String lastName;
    private final long paymentAddressId;
    private final long shippingAddressId;

    public Order(LocalDateTime orderDate, long customerId, boolean paid, String paymentCode, long paymentId, long orderStatusId, boolean dealCodeUsed, String companyName, String BTWNumber, String firstName, String lastName, long paymentAddressId, long shippingAddressId, long id) {
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.paid = paid;
        this.paymentCode = paymentCode;
        this.paymentId = paymentId;
        this.orderStatusId = orderStatusId;
        this.dealCodeUsed = dealCodeUsed;
        this.companyName = companyName;
        this.BTWNumber = BTWNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.paymentAddressId = paymentAddressId;
        this.shippingAddressId = shippingAddressId;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public long getCustomerId() {
        return customerId;
    }

    public boolean isPaid() {
        return paid;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public boolean isDealCodeUsed() {
        return dealCodeUsed;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getBTWNumber() {
        return BTWNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getPaymentAddressId() {
        return paymentAddressId;
    }

    public long getShippingAddressId() {
        return shippingAddressId;
    }

    public long getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(long orderStatusId) {
        this.orderStatusId = orderStatusId;
    }
}
