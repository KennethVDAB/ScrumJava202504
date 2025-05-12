package be.vdab.scrumjava202504.products;

import be.vdab.scrumjava202504.ProductDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{artikelId}")
    public List<ProductDTO> getPlaceForArtikel(@PathVariable long artikelId) {
        return productService.getPlaceForArtikel(artikelId);
    }
}
