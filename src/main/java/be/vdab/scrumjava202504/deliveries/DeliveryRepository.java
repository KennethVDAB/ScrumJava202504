package be.vdab.scrumjava202504.deliveries;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryRepository {
    private final JdbcClient jdbcClient;

    public DeliveryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    /**
     * Creates a new delivery record in the database.
     *
     * @param delivery the delivery object containing the details of the delivery
     * @return the ID of the newly created delivery record
     */
    public long create(Delivery delivery) {
        var sql = """
                INSERT INTO inkomendeleveringen(leveranciersId, leveringsbonNummer, leveringsbonDatum, leverDatum, ontvangerPersoneelslidId)
                VALUES (?, ?, ?, ?, ?);
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .params(delivery.getSupplierId(), delivery.getDeliveryTicketNumber(), delivery.getDeliveryTicketDate(), delivery.getDeliveryDate(), delivery.getRecipientId())
                .update(keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * Inserts a new delivery line into the database.
     *
     * @param deliveryLine the delivery line to insert (articleId must already be resolved)
     */
    public void create(NewDeliveryLine deliveryLine) {
        var sql = """
            INSERT INTO InkomendeLeveringsLijnen 
            (inkomendeLeveringsId, artikelId, aantalGoedgekeurd, aantalTeruggestuurd, magazijnPlaatsId)
            VALUES (?, ?, ?, ?, ?)
            """;
        jdbcClient.sql(sql)
                .params(
                        deliveryLine.getIncomingDeliveryId(),
                        deliveryLine.getArticleId(),
                        deliveryLine.getApprovedAmount(),
                        deliveryLine.getReturnedAmount(),
                        deliveryLine.getWarehouseLocationId()
                )
                .update();
    }
}
