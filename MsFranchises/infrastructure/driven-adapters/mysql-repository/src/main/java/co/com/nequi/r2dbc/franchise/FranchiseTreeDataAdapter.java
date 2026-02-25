package co.com.nequi.r2dbc.franchise;

import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.franchise.gateways.FranchiseTreeGateway;
import co.com.nequi.model.franchise.query.FranchiseTreeQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FranchiseTreeDataAdapter implements FranchiseTreeGateway {

    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<Transaction> retrieveTree(Transaction tx) {
        var q = (FranchiseTreeQuery) tx.getRequest();
        return franchiseRepository.retrieveTree(q)
                .map(page -> {
                    tx.setResponse(page);
                    return tx;
                });
    }
}