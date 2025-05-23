package be.vdab.scrumjava202504.products;

public class SimpleProductDTO {
    private String name;
    private String ean;
    private long productId;

    public SimpleProductDTO(String ean, long productId, String name) {
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

    public long getProductId() {
        return productId;
    }
}
