package be.vdab.scrumjava202504.products;

/**
 * Represents an order with all the necessary information for pickup
 */

public class ProductDTO {
    private String shelf;
    private int position;
    private String name;
    private int quantity;
    private int productId;

    public ProductDTO(String shelf, int position, String name, int quantity, int productId) {
        this.shelf = shelf;
        this.position = position;
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
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

    /**
     * Returns the productId of the product
     */
    public int getProductId() {
        return productId;
    }

    public String toString() {
    	return "ProductDTO [shelf=" + shelf + ", position=" + position + ", name=" + name + ", quantity=" + quantity + ", productId=" + productId + "]";
    }

}
