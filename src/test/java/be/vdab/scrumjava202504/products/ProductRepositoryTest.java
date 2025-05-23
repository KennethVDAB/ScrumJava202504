package be.vdab.scrumjava202504.products;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindByArtikelId_returnsCorrectProducts() {
        long artikelId = 1;

        List<ProductDTO> results = productRepository.findByArtikelId(artikelId);

        assertThat(results).hasSize(2);

        ProductDTO first = results.get(0);
        assertThat(first.getQuantity()).isEqualTo(25);
        assertThat(first.getShelf()).isEqualTo("A");
        assertThat(first.getPosition()).isEqualTo(1);
        assertThat(first.getName()).isEqualTo("emmer 10 l");
        assertThat(first.getProductId()).isEqualTo(1);

        ProductDTO second = results.get(1);
        assertThat(second.getQuantity()).isEqualTo(25);
        assertThat(second.getShelf()).isEqualTo("A");
        assertThat(second.getPosition()).isEqualTo(2);
    }

}

