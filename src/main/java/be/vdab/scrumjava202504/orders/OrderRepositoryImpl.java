package be.vdab.scrumjava202504.orders;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<DisplayOrder> getDisplayOrders() {
        String sql = """
                SELECT
                    od.bestelId as id,
                    COUNT(od.artikelId) AS products,
                    SUM(p.gewichtInGram) / 1000 AS weight
                FROM
                    bestellijnen od
                JOIN
                    artikelen p ON od.artikelId = p.artikelId
                JOIN
                	bestellingen b on b.bestelId = od.bestelId
                WHERE b.bestellingsstatusid = 2
                GROUP BY
                    od.bestelId
                ORDER BY
                    b.besteldatum
                LIMIT 5
                """;
        return jdbcClient.sql(sql)
                .query(DisplayOrder.class)
                .list();
    }

    public List<OrderDetails> getOrderDetailsByOrderId(long orderId) {
        String sql = """
                SELECT bestellijnid AS orderDetailId, bestelid AS orderId, artikelid AS productid, aantalBesteld AS quantityOrder
                FROM bestellijnen
                WHERE bestelid = ?
                """;

        return jdbcClient.sql(sql)
                .param(orderId)
                .query(OrderDetails.class)
                .list();
    }
}