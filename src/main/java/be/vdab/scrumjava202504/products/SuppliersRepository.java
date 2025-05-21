package be.vdab.scrumjava202504.products;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SuppliersRepository {
    private final JdbcClient jdbcClient;

    public SuppliersRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    List<SupplierDTO> fetchAllSupplierNames() {
        var sql = """
                SELECT leveranciers.leveranciersId as id, leveranciers.naam as name
                FROM leveranciers""";
        return jdbcClient.sql(sql)
                .query(SupplierDTO.class)
                .list();
    }
}
