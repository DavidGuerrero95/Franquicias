package co.com.nequi.r2dbc.product;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductData, Long>,
        ReactiveQueryByExampleExecutor<ProductData> {

    Mono<ProductData> findByBranchIdAndName(Long branchId, String name);

    @Query("""
        SELECT
          b.id   AS branchId,
          b.name AS branchName,
          p.id   AS productId,
          p.name AS productName,
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

    interface TopStockRow {
        Long getBranchId();
        String getBranchName();
        Long getProductId();
        String getProductName();
        Integer getStock();
    }
}