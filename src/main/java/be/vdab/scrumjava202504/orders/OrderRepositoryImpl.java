package be.vdab.scrumjava202504.orders;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
                WHERE betaald = TRUE
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

    public Optional<Order> findAndLockById(long orderId) {
        String sql = """
                SELECT bestelId as id, besteldatum as orderDate, klantId as customerId, betaald as paid, betalingscode as paymentCode, betaalwijzeId as paymentId, bestellingsStatusId as orderStatusId, actiecodeGebruikt as dealCodeUsed, bedrijfsnaam as companyName, btwNummer as BTWNumber, voornaam as firstName, familienaam as lastName, facturatieAdresId as paymentAddressId, leveringsAdresId as shippingAddressId
                FROM bestellingen
                WHERE bestelId = ?""";
        return jdbcClient.sql(sql)
                .param(orderId)
                .query(Order.class)
                .optional();
    }

    public void updateOrderStatus(long orderId, long orderStatusId) {
        String sql = """
                UPDATE bestellingen
                SET bestellingsStatusId = ?
                WHERE bestelId = ?""";
        jdbcClient.sql(sql)
                .params(orderStatusId, orderId)
                .update();
    }
}