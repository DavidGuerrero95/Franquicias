package co.com.nequi.model.product.gateways;

import co.com.nequi.dto.transaction.Transaction;
import reactor.core.publisher.Mono;

public interface ProductGateway {
    Mono<Transaction> getProductDetail(Transaction transaction);
}