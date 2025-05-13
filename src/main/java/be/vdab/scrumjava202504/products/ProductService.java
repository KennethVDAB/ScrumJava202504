package be.vdab.scrumjava202504.products;

import be.vdab.scrumjava202504.ProductDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    public List<ProductDTO> getPlaceForArtikel(long artikelId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
