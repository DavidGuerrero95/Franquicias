package co.com.nequi.model.product.gateways;

import co.com.nequi.dto.transaction.Transaction;
import reactor.core.publisher.Mono;

public interface ProductGateway {
    Mono<Transaction> create(Transaction transaction);
    Mono<Transaction> rename(Transaction transaction);
    Mono<Transaction> updateStock(Transaction transaction);
    Mono<Transaction> delete(Transaction transaction);
    Mono<Transaction> findById(Transaction transaction);
    Mono<Transaction> findByName(Transaction transaction);
    Mono<Transaction> topStockByFranchise(Transaction transaction);
}