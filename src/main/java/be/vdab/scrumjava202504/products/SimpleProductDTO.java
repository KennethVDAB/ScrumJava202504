package be.vdab.scrumjava202504.products;

public class SimpleProductDTO {
    private String name;
    private String ean;
    private int productId;

    public SimpleProductDTO(String ean, int productId, String name) {
        this.name = name;
        this.ean = ean;
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public String getEan() {
        return ean;
    }

    public int getProductId() {
        return productId;
    }
}
