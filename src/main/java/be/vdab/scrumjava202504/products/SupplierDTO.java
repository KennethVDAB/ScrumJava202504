package be.vdab.scrumjava202504.products;

public class SupplierDTO {
    private final long id;
    private final String name;

    public SupplierDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
