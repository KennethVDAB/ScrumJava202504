package be.vdab.scrumjava202504.deliveries;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class NewDeliveryLine {

    @NotNull
    private Integer incomingDeliveryId;

    @NotNull
    private String ean;

    @NotNull
    @Min(1)
    private Integer approvedAmount;

    @NotNull
    @Min(0)
    private Integer returnedAmount;

    @NotNull
    private Integer warehouseLocationId;

    private Long articleId;

    public Integer getIncomingDeliveryId() {
        return incomingDeliveryId;
    }

    public void setIncomingDeliveryId(Integer incomingDeliveryId) {
        this.incomingDeliveryId = incomingDeliveryId;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public Integer getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(Integer approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public Integer getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(Integer returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public Integer getWarehouseLocationId() {
        return warehouseLocationId;
    }

    public void setWarehouseLocationId(Integer warehouseLocationId) {
        this.warehouseLocationId = warehouseLocationId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}
