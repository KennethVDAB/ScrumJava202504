package be.vdab.scrumjava202504;

/**
 * Represents an order with all the necessary information for pickup
 */

public class ProductDTO {
    private String shelf;
    private int position;
    private String name;
    private int quantity;

    public ProductDTO(String shelf, int position, String naam, int quantity) {
        this.shelf = shelf;
        this.position = position;
        this.name = naam;
        this.quantity = quantity;
    }

    /**
     * Returns the shelf where the product is stored in the warehouse
     */
    public String getShelf() {
        return shelf;
    }

    /**
     * Returns the position where the product is stored in the warehouse (ex. shelf A position 1)
     */
    public int getPosition() {
        return position;
    }

    /**
     * Returns the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the quantity of the ordered amount of the product
     */
    public int getQuantity() {
        return quantity;
    }

}
