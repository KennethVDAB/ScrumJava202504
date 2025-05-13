package be.vdab.scrumjava202504.products;

import be.vdab.scrumjava202504.ProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{artikelId}/plaatsen")
    public List<ProductDTO> getPlaceForArtikel(@PathVariable long artikelId) {
        var plaatsen = productService.getPlaceForArtikel(artikelId);
        if (plaatsen.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Geen locaties gevonden voor artikel " + artikelId);
        }
        return plaatsen;
    }
}
