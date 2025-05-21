package be.vdab.scrumjava202504.orders;

import be.vdab.scrumjava202504.orders.OrderRepositoryImpl;
import be.vdab.scrumjava202504.orders.DisplayOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(OrderRepositoryImpl.class)
@Sql("/orders.sql")
class OrderRepositoryImplTest {

    @Autowired
    private OrderRepositoryImpl orderRepositoryImpl;

    @Autowired
    private JdbcClient jdbcClient;

    @Test
    void getOrdersCountReturnsNumberOfPaidOrders() {
        var expected = jdbcClient.sql("SELECT COUNT(*) FROM bestellingen WHERE betaald = TRUE")
                .query(Long.class).single();
        assertThat(orderRepositoryImpl.getOrdersCount()).isEqualTo(expected);
    }

    @Test
    void getDisplayOrdersReturnsAtMostFiveResults() {
        var displayOrders = orderRepositoryImpl.getDisplayOrders();
        assertThat(displayOrders).hasSizeLessThanOrEqualTo(5);
    }

    @Test
    void getDisplayOrdersDoesNotIncludeUnpaidOrders() {
        var displayOrders = orderRepositoryImpl.getDisplayOrders();
        var unpaidOrderId = 102L;
        assertThat(displayOrders)
                .extracting(DisplayOrder::getId)
                .doesNotContain(unpaidOrderId);
    }

    @Test
    void getDisplayOrdersIsSortedByOrderDate() {
        var displayOrders = orderRepositoryImpl.getDisplayOrders();
        assertThat(displayOrders)
                .extracting(DisplayOrder::getId)
                .containsExactly(100L, 101L, 103L, 105L);
    }

    @Test
    void getDisplayOrdersCalculatesTotalWeightCorrectly() {
        var displayOrders = orderRepositoryImpl.getDisplayOrders();
        var weight = displayOrders.stream()
                .filter(order -> order.getId() == 100L)
                .map(DisplayOrder::getWeight)
                .findFirst()
                .orElseThrow();
        assertThat(weight).isEqualByComparingTo(new BigDecimal("2"));
    }
}
