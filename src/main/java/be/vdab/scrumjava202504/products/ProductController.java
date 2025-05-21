package be.vdab.scrumjava202504.products;

import be.vdab.scrumjava202504.exception.ProductNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{artikelId}/places")
    public List<ProductDTO> getPlaceForArtikel(@PathVariable long artikelId) {
        var plaatsen = productService.getPlaceForArtikel(artikelId);
        if (plaatsen.isEmpty()) {
            throw new ProductNotFoundException(artikelId);
        }
        return plaatsen;
    }

    @GetMapping("/{artikelId}/{shelf}/{position}")
    public List<ProductDetails> findProductDetailsByArtikelIdAndPlace( @PathVariable long artikelId, @PathVariable String shelf, @PathVariable int position){
        return productService.findProductDetailsByArtikelIdAndPlace(artikelId, shelf, position);
    }
}
