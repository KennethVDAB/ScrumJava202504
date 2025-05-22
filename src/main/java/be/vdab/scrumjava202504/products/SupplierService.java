package be.vdab.scrumjava202504.products;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    private final SuppliersRepository suppliersRepository;

    public SupplierService(SuppliersRepository suppliersRepository) {
        this.suppliersRepository = suppliersRepository;
    }

    /**
     * Fetches all supplier names.
     *
     * @return a list of SupplierDTO objects containing supplier IDs and names
     */
    public List<SupplierDTO> getAllSupplierNames() {
        return suppliersRepository.fetchAllSupplierNames();
    }

    /**
     * Fetches the supplier ID based on the supplier name.
     *
     * @param name the name of the supplier
     * @return an Optional containing the supplier ID if found, or an empty Optional if not found
     */
    public Optional<Long> getSupplierIdByName(String name) {
        return suppliersRepository.fetchSupplierIdByName(name);
    }
}
