package be.vdab.scrumjava202504.deliveries;

public class DeliveryLine {
    private final int incomingDeliveryId;
    private final int articleId;
    private final int approvedAmount;
    private final int returnedAmount;
    private final int warehouseLocationId;

    public DeliveryLine(int incomingDeliveryId, int articleId, int approvedAmount, int returnedAmount, int warehouseLocationId) {
        this.incomingDeliveryId = incomingDeliveryId;
        this.articleId = articleId;
        this.approvedAmount = approvedAmount;
        this.returnedAmount = returnedAmount;
        this.warehouseLocationId = warehouseLocationId;
    }

    public int getIncomingDeliveryId() {
        return incomingDeliveryId;
    }

    public int getArticleId() {
        return articleId;
    }

    public int getApprovedAmount() {
        return approvedAmount;
    }

    public int getReturnedAmount() {
        return returnedAmount;
    }

    public int getWarehouseLocationId() {
        return warehouseLocationId;
    }
}
