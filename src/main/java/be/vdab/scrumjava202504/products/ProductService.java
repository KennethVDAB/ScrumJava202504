package be.vdab.scrumjava202504.products;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getPlaceForArtikel (long artikelId) {
        return productRepository.findByArtikelId(artikelId);
    }

    //TODO: connect all the updates together
}
