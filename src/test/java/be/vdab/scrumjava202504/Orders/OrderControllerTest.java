package be.vdab.scrumjava202504.Orders;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class OrderControllerTest {
    private final String ORDERS_TABLE_NAME = "bestellingen";
    private final MockMvcTester mockMvcTester;
    private final JdbcClient jdbcClient;

    @Test
    void findOrdersCount_Success() {
        MockMvcTester.MockMvcRequestBuilder response = mockMvcTester.get()
                .uri("/api/order/count");
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .isEqualTo(JdbcTestUtils
                        .countRowsInTableWhere(jdbcClient, ORDERS_TABLE_NAME, "betaald = 1"));
    }

    @Test
    void findDisplayOrders_Success() {
        var response = mockMvcTester.get()
                .uri("/api/order/display");

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.length()")
                .satisfies(length -> assertThat((int) length).isLessThanOrEqualTo(5));
    }

}
