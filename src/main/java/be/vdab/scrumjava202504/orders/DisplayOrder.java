package be.vdab.scrumjava202504.orders;

import java.math.BigDecimal;

/**
 * Represents a display order with an ID, number of products, and total weight to be displayed on the screen.
 */
public class DisplayOrder {
    private final long id;
    private final int products;
    private final BigDecimal weight;

    public DisplayOrder(long id, int products, BigDecimal weight) {
        this.id = id;
        this.products = products;
        this.weight = weight;
    }

    /**
     * @return The ID of the order.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The number of products in the order.
     */
    public int getProducts() {
        return products;
    }

    /**
     * @return The total weight of the order.
     */
    public BigDecimal getWeight() {
        return weight;
    }
}
