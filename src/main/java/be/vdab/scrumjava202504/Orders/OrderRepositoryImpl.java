package be.vdab.scrumjava202504.Orders;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final JdbcClient jdbcClient;

    public OrderRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public long getOrdersCount() {
        String sql = """
                SELECT COUNT(*)
                FROM Bestellingen
                WHERE betaald = 1
                """;

        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }
}