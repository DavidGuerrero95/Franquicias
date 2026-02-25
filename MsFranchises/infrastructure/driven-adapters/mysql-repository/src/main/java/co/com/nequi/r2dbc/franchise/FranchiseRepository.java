package co.com.nequi.r2dbc.franchise;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepository extends ReactiveCrudRepository<FranchiseData, Long>,
        ReactiveQueryByExampleExecutor<FranchiseData>,
        FranchiseTreeRepository {

    Mono<FranchiseData> findByName(String name);
}