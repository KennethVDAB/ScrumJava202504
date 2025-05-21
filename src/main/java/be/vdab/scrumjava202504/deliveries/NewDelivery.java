package be.vdab.scrumjava202504.deliveries;

import java.util.Date;

public class NewDelivery {
    private final String supplierName;
    private final String deliveryTicketNumber;
    private final Date deliveryTicketDate;
    private final Date deliveryDate;

    public NewDelivery(String supplierName, String deliveryTicketNumber, Date deliveryTicketDate, Date deliveryDate) {
        this.supplierName = supplierName;
        this.deliveryTicketNumber = deliveryTicketNumber;
        this.deliveryTicketDate = deliveryTicketDate;
        this.deliveryDate = deliveryDate;
    }

    public String getSupplierName() {
        return supplierName;
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
}
