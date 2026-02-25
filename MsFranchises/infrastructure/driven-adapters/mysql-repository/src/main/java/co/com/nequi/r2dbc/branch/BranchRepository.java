package co.com.nequi.r2dbc.branch;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BranchRepository extends ReactiveCrudRepository<BranchData, Long>,
        ReactiveQueryByExampleExecutor<BranchData> {

    Mono<BranchData> findByFranchiseIdAndName(Long franchiseId, String name);

    Flux<BranchData> findByFranchiseIdInOrderByFranchiseIdAscIdAsc(List<Long> franchiseIds);

    Flux<BranchData> findByFranchiseIdInAndNameContainingOrderByFranchiseIdAscIdAsc(List<Long> franchiseIds, String name);
}