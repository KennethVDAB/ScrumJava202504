package be.vdab.scrumjava202504.orders;

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
                .uri("/orders/count");
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .isEqualTo(JdbcTestUtils
                        .countRowsInTableWhere(jdbcClient, ORDERS_TABLE_NAME, "betaald = 1"));
    }
}
