package be.vdab.scrumjava202504.orders;

import be.vdab.scrumjava202504.products.ProductRepository;
import be.vdab.scrumjava202504.products.ProductService;
import be.vdab.scrumjava202504.warehouseLocations.WarehouseLocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({OrderService.class, OrderRepositoryImpl.class, ProductRepository.class,ProductService.class, WarehouseLocationRepository.class})
@Sql("/orders.sql")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void getOrdersCountReturnsPositiveValue() {
        assertThat(orderService.getOrdersCount()).isPositive();
    }

    @Test
    void getDisplayOrdersReturnsAtMostFiveOrders() {
        var orders = orderService.getDisplayOrders();
        assertThat(orders).hasSizeLessThanOrEqualTo(5);
    }
}
