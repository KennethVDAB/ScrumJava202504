package be.vdab.scrumjava202504.deliveries;

import java.util.Date;

public class Delivery {
    private final long deliveryId;
    private final long supplierId;
    private final String deliveryTicketNumber;
    private final Date deliveryTicketDate;
    private final Date deliveryDate;
    private final int recipientId = 4;

    public Delivery(long deliveryId, long supplierId, String deliveryTicketNumber, Date deliveryTicketDate, Date deliveryDate) {
        this.deliveryId = deliveryId;
        this.supplierId = supplierId;
        this.deliveryTicketNumber = deliveryTicketNumber;
        this.deliveryTicketDate = deliveryTicketDate;
        this.deliveryDate = deliveryDate;
    }

    public long getDeliveryId() {
        return deliveryId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public String getDeliveryTicketNumber() {
        return deliveryTicketNumber;
    }

    public Date getDeliveryTicketDate() {
        return deliveryTicketDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public int getRecipientId() {
        return recipientId;
    }
}
