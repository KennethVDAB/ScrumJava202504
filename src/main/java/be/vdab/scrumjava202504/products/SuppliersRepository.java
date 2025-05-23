package be.vdab.scrumjava202504.products;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * Fetches the supplier ID based on the supplier name.
     *
     * @param name the name of the supplier
     * @return an Optional containing the supplier ID if found, or an empty Optional if not found
     */
    public Optional<Long> fetchSupplierIdByName(String name) {
        var sql = """
                SELECT leveranciers.leveranciersId as id
                FROM leveranciers
                WHERE leveranciers.naam = ?""";
        return jdbcClient.sql(sql)
                .param(name)
                .query(long.class)
                .optional();
    }
}
