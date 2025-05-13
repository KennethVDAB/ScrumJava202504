package be.vdab.scrumjava202504.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(long artikelId) {
        super("No locations found for article with ID " + artikelId);
    }
}
