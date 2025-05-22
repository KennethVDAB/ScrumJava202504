package be.vdab.scrumjava202504.deliveries;

/**
 * Represents an instruction for placing a product in the warehouse.
 */
public class PlacementItem {
    private final String shelf;
    private final int position;
    private final String name;
    private final int quantityToPlace;
    private final long productId;

    public PlacementItem(String shelf, int position, String name, int quantityToPlace, long productId) {
        this.shelf = shelf;
        this.position = position;
        this.name = name;
        this.quantityToPlace = quantityToPlace;
        this.productId = productId;
    }

    public String getShelf() {
        return shelf;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getQuantityToPlace() {
        return quantityToPlace;
    }

    public long getProductId() {
        return productId;
    }
}
