package be.vdab.scrumjava202504.products;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    public void updateStock(long productId, BigDecimal inStock) {
        var sql = """
                UPDATE Artikelen
                SET voorraad = ?
                WHERE artikelId = ?
                """;
        jdbcClient.sql(sql)
                .params(inStock, productId)
                .update();
    }

    public Optional<Product> findAndLockByArtikelId(long artikelId) {
        var sql = """
                SELECT artikelId as productId, ean, naam as name, omschrijving as description, prijs as price, gewichtInGram as weightInGram, voorraad as inStock, minVoorraad as minStock, maxVoorraad as maxStpcl, levertijd as deliveryTime, besteldBijLeverancier as orderedAtSupplier, maxInStockPlaats as maxInStockPlace, leverancierId as supplierId
                FROM Artikelen
                WHERE artikelId = ?
                FOR UPDATE
                """;
        return jdbcClient.sql(sql)
                .param(artikelId)
                .query(Product.class)
                .optional();
    }
}
