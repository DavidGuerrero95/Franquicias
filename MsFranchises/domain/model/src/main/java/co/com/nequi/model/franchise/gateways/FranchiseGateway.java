package co.com.nequi.model.franchise.gateways;

import co.com.nequi.dto.transaction.Transaction;
import reactor.core.publisher.Mono;

public interface FranchiseGateway {
    Mono<Transaction> create(Transaction transaction);
    Mono<Transaction> rename(Transaction transaction);
    Mono<Transaction> findById(Transaction transaction);
    Mono<Transaction> findByName(Transaction transaction);
}