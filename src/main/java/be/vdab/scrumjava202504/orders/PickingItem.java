package be.vdab.scrumjava202504.orders;

public class PickingItem {
    private String shelf;
    private int position;
    private String name;
    private int pickedQuantity;
    private long productId;
    private boolean picked = false;

    public PickingItem(String shelf, int position, String name, int quantity, long productId) {
        this.shelf = shelf;
        this.position = position;
        this.name = name;
        this.pickedQuantity = quantity;
        this.productId = productId;
    }

    public int getPosition() {
        return position;
    }

    public String getShelf() {
        return shelf;
    }

    public String getName() {
        return name;
    }

    public int getPickedQuantity() {
        return pickedQuantity;

    public long getProductId() {
        return productId;
    }

    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public long getProductId() {return productId;}
}
