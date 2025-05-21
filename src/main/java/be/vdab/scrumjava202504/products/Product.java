package be.vdab.scrumjava202504.products;

import java.math.BigDecimal;

public class Product {
    private final long productId;
    private final long ean;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final BigDecimal weightInGram;
    private BigDecimal inStock;
    private final BigDecimal minStock;
    private final BigDecimal maxStock;
    private final int deliveryTime;
    private final BigDecimal orderedAtSupplier;
    private final BigDecimal maxInStockPlace;
    private final int supplierId;

    public Product(long productId, long ean, String name, String description, BigDecimal price, BigDecimal weightInGram, BigDecimal inStock, BigDecimal minStock, BigDecimal maxStock, int deliveryTime, BigDecimal orderedAtSupplier, BigDecimal maxInStockPlace, int supplierId) {
        this.productId = productId;
        this.ean = ean;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weightInGram = weightInGram;
        this.inStock = inStock;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.deliveryTime = deliveryTime;
        this.orderedAtSupplier = orderedAtSupplier;
        this.maxInStockPlace = maxInStockPlace;
        this.supplierId = supplierId;
    }

    public long getProductId() {
        return productId;
    }

    public long getEan() {
        return ean;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getWeightInGram() {
        return weightInGram;
    }

    public BigDecimal getInStock() {
        return inStock;
    }

    public BigDecimal getMinStock() {
        return minStock;
    }

    public BigDecimal getMaxStock() {
        return maxStock;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public BigDecimal getOrderedAtSupplier() {
        return orderedAtSupplier;
    }

    public BigDecimal getMaxInStockPlace() {
        return maxInStockPlace;
    }

    public int getSupplierId() {
        return supplierId;
    }

    /**
     * Sets the stock of the product.
     * check if the ordered amount is less than what is in stock is already checked
     * @param ordered The amount that was ordered.
     */
    public void setStock(BigDecimal ordered) {
        inStock = inStock.subtract(ordered);
    }
}
