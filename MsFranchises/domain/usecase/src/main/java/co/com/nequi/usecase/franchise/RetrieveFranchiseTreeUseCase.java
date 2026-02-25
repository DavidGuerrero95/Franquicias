package co.com.nequi.usecase.franchise;

import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.franchise.gateways.FranchiseTreeGateway;
import co.com.nequi.usecase.util.UseCaseErrorMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RetrieveFranchiseTreeUseCase {

    private final FranchiseTreeGateway gateway;

    public Mono<Transaction> execute(Transaction tx) {
        return gateway.retrieveTree(tx)
                .map(out -> {
                    out.setStatus(true);
                    out.setStatusCode(200);
                    return out;
                })
                .onErrorMap(UseCaseErrorMapper::map);
    }
}