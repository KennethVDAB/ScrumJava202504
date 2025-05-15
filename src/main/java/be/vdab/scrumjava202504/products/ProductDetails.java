package be.vdab.scrumjava202504.products;

public class ProductDetails {
    private String productName;
    private long EAN;
    private String shelf;
    private int position;
    private int quantityOrdered;
    private double price;
    private int quantityStock;
    private String supplier;
    private double weight;

    public ProductDetails(String productName, long EAN, String shelf, int position, int quantityOrdered, double price, int quantityStock, String supplier, double weight) {
        this.productName = productName;
        this.EAN = EAN;
        this.shelf = shelf;
        this.position = position;
        this.quantityOrdered = quantityOrdered;
        this.price = price;
        this.quantityStock = quantityStock;
        this.supplier = supplier;
        this.weight = weight;
    }

    public long getEAN() {
        return EAN;
    }

    public String getShelf() {
        return shelf;
    }

    public int getPosition() {
        return position;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantityStock() {
        return quantityStock;
    }

    public String getProductName() {
        return productName;}

    public String getSupplier() {
        return supplier;
    }

    public double getWeight() {
        return weight;
    }
}
