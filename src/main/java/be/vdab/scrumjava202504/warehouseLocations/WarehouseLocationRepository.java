package be.vdab.scrumjava202504.warehouseLocations;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
}
