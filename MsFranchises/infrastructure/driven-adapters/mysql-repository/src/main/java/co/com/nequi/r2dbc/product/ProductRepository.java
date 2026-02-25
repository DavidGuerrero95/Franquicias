package co.com.nequi.r2dbc.product;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductRepository extends ReactiveCrudRepository<ProductData, Long>,
        ReactiveQueryByExampleExecutor<ProductData> {

    Mono<ProductData> findByBranchIdAndName(Long branchId, String name);

    Flux<ProductData> findByBranchIdInOrderByBranchIdAscIdAsc(List<Long> branchIds);

    @Query("""
            SELECT
              b.id   AS branch_id,
              b.name AS branch_name,
              p.id   AS product_id,
              p.name AS product_name,
              p.stock AS stock
            FROM branch b
            JOIN (
                SELECT id, branch_id, name, stock,
                       ROW_NUMBER() OVER(PARTITION BY branch_id ORDER BY stock DESC, id ASC) rn
                FROM product
            ) p ON p.branch_id = b.id AND p.rn = 1
            WHERE b.franchise_id = :franchiseId
            ORDER BY b.id
            """)
    Flux<TopStockRow> findTopStockByFranchiseId(Long franchiseId);

    record TopStockRow(
            Long branch_id,
            String branch_name,
            Long product_id,
            String product_name,
            Integer stock
    ) {
    }

}