package co.com.nequi.usecase.product;

import co.com.nequi.dto.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RetrieveProductDetailUseCase {

    //private final ProductGateway gateway;

    public Mono<Transaction> retrieve(Transaction transaction) {
        return Mono.defer(() -> Mono.just(transaction));
    }

}