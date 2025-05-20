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

    Optional<WarehouseLocation> findAndLockById(int id) {
        String sql = """
                SELECT magazijnPlaatsId as id, artikelId as productId, rij as shelf, rek as position, aantal as amount
                FROM magazijnplaatsen
                WHERE magazijnPlaatsId = ?
                FOR UPDATE
                """;

        return jdbcClient.sql(sql)
                .param(id)
                .query(WarehouseLocation.class)
                .optional();
    }

    void updateAmount(int id, BigDecimal amount) {
        String sql = """
                UPDATE magazijnplaatsen
                SET aantal = ?
                WHERE magazijnPlaatsId = ?
                """;

        jdbcClient.sql(sql)
                .params(amount, id)
                .update();
    }
}
