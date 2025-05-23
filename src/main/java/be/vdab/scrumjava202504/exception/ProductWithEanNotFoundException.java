package be.vdab.scrumjava202504.exception;

public class ProductWithEanNotFoundException extends RuntimeException {
    public ProductWithEanNotFoundException(String ean) {
        super("Product with ean " + ean + " not found");
    }
}
