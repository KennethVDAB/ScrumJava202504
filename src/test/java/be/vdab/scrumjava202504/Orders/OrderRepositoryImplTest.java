package be.vdab.scrumjava202504.Orders;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(OrderRepositoryImpl.class)
@Sql("/orders.sql") // This file sets up test data
class OrderRepositoryImplTest {

    @Autowired
    private OrderRepositoryImpl repository;
    @Autowired
    private JdbcClient jdbcClient;

    private static final String ORDERS_TABLE = "bestellingen";

    @Test
    void getOrdersCountReturnsCorrectNumberOfPaidOrders() {
        var expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcClient, ORDERS_TABLE, "betaald IS TRUE");
        assertThat(repository.getOrdersCount()).isEqualTo(expectedCount);
    }

    @Test
    void getDisplayOrdersReturnsAtMostFiveOrders() {
        var orders = repository.getDisplayOrders();
        assertThat(orders.size()).isLessThanOrEqualTo(5);
    }

    @Test
    void getDisplayOrdersReturnsOrdersWithValidFields() {
        var orders = repository.getDisplayOrders();
        if (!orders.isEmpty()) {
            var order = orders.get(0);
            assertThat(order.getId()).isPositive();
            assertThat(order.getProducts()).isPositive();
            assertThat(order.getWeight()).isInstanceOf(BigDecimal.class);
        }
    }
}
