package be.vdab.scrumjava202504.warehouseLocations;

import java.math.BigDecimal;

public class WarehouseLocation {
    private final long id;
    private final long productId;
    private final String shelf;
    private final int position;
    private BigDecimal amount;

    public WarehouseLocation(long id, long productId, String shelf, int position, BigDecimal amount) {
        this.id = id;
        this.productId = productId;
        this.shelf = shelf;
        this.position = position;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }

    public String getShelf() {
        return shelf;
    }

    public int getPosition() {
        return position;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Updates the amount of the warehouse location.
     * If the picked amount is equal to the current amount, it sets the amount to zero.
     * Otherwise, it subtracts the picked amount from the current amount.
     *
     * @param picked The amount picked from the warehouse location.
     */
    public void updateAmount(BigDecimal picked) {
        if (picked.compareTo(amount) == 0) {
            amount = BigDecimal.ZERO;
        } else {
            amount = amount.subtract(picked);
        }
    }
}
