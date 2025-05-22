package be.vdab.scrumjava202504.products;

import java.util.Optional;

import be.vdab.scrumjava202504.exception.ProductNotFoundException;
import be.vdab.scrumjava202504.exception.ProductWithEanNotFoundException;
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

    public List<ProductDetails> findProductDetailsByArtikelIdAndPlace(long artikelid, String shelf, int row) {
        return productRepository.findProductDetailsByArtikelIdAndPlace(artikelid, shelf, row);
    }

    public SimpleProductDTO findProductByEanNumber(String ean) {
        return productRepository.findProductByEanNumber(ean)
                .orElseThrow(() -> new ProductWithEanNotFoundException(ean));
    }
}
