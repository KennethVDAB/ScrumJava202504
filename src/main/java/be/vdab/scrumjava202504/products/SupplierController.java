package be.vdab.scrumjava202504.products;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/supplier")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/all")
    public List<SupplierDTO> getAllSupplierNames() {
        return supplierService.getAllSupplierNames();
    }

    public Optional<Long> getSupplierIdByName(String name) {
        return supplierService.getSupplierIdByName(name);
    }
}
