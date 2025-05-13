package be.vdab.scrumjava202504.products;

import be.vdab.scrumjava202504.ProductDTO;
import org.springframework.jdbc.core.JdbcTemplate;
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
                SELECT place.rij,place.rek,place.aantal, artikelen.naam AS artikelName
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
