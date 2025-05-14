package be.vdab.scrumjava202504.products;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ProductRepository {

    private final JdbcClient jdbcClient;

    public ProductRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<ProductDTO> findByArtikelId(long artikelId) {
        var sql = """
                SELECT place.rij AS shelf, place.rek AS position, place.aantal AS quantity, artikel.naam AS name, artikel.artikelId AS productId
                FROM MagazijnPlaatsen AS place
                INNER JOIN Artikelen AS artikel ON place.artikelId = artikel.artikelId
                WHERE place.artikelId = ?
                ORDER BY place.aantal DESC 
                """;
        return jdbcClient.sql(sql)
                .param(artikelId)
                .query(ProductDTO.class)
                .list();
    }
}
