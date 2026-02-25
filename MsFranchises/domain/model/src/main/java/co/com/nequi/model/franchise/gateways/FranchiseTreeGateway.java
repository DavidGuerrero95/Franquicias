package co.com.nequi.model.franchise.gateways;

import co.com.nequi.dto.transaction.Transaction;
import reactor.core.publisher.Mono;

public interface FranchiseTreeGateway {
    Mono<Transaction> retrieveTree(Transaction transaction);
}