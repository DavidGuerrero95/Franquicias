package co.com.nequi.r2dbc.franchise;

import co.com.nequi.model.franchise.query.FranchiseTreeQuery;
import co.com.nequi.model.franchise.report.FranchiseTreePage;
import reactor.core.publisher.Mono;

public interface FranchiseTreeRepository {
    Mono<FranchiseTreePage> retrieveTree(FranchiseTreeQuery query);
}