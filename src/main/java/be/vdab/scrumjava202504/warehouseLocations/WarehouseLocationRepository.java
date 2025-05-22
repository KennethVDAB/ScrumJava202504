package be.vdab.scrumjava202504.warehouseLocations;

import be.vdab.scrumjava202504.products.ProductDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class WarehouseLocationRepository {
    private final JdbcClient jdbcClient;

    public WarehouseLocationRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<WarehouseLocation> findBySelfAndPositionAndLockById(String shelf, int position) {
        String sql = """
                SELECT magazijnPlaatsId as id, artikelId as productId, rij as shelf, rek as position, aantal as amount
                FROM magazijnplaatsen
                WHERE rij = ? AND rek = ?
                FOR UPDATE
                """;

        return jdbcClient.sql(sql)
                .params(shelf, position)
                .query(WarehouseLocation.class)
                .optional();
    }

    public void updateAmount(String shelf, int position, BigDecimal amount) {
        String sql = """
                UPDATE magazijnplaatsen
                SET aantal = ?
                WHERE rij = ? AND rek = ?
                """;

        jdbcClient.sql(sql)
                .params(amount, shelf, position)
                .update();
    }

    /**
     * Retrieves all warehouse locations sorted by shelf (A-Z) and position (1-60).
     * This method is used to determine the most logical walking route for placing products.
     *
     * @return A list of {@link ProductDTO} objects representing all warehouse locations.
     */
    public List<ProductDTO> findAllLocationsSorted() {
        var sql = """
        SELECT 
            plaats.rij AS shelf,
            plaats.rek AS position,
            COALESCE(artikel.naam, '') AS name,
            plaats.aantal AS quantity,
            COALESCE(plaats.artikelId, 0) AS productId
        FROM MagazijnPlaatsen plaats
        LEFT JOIN Artikelen artikel ON plaats.artikelId = artikel.artikelId
        ORDER BY plaats.rij ASC, plaats.rek ASC
    """;
        return jdbcClient.sql(sql).query(ProductDTO.class).list();
    }


}
