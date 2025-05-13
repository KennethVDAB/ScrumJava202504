package be.vdab.scrumjava202504.products;

import be.vdab.scrumjava202504.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getPlaceForArtikel (long artikelId) {
        return productRepository.findByArtikelId(artikelId);
    }
}
