package co.com.nequi.usecase.product;

import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.gateways.ProductGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static co.com.nequi.usecase.product.datatest.TestDataRetrieveProductDetail.DEFAULT_ID;
import static co.com.nequi.usecase.product.datatest.TestDataRetrieveProductDetail.productQuery;
import static co.com.nequi.usecase.product.datatest.TestDataRetrieveProductDetail.txWithRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveProductDetailUseCaseTest {

    @Mock
    private ProductGateway gateway;

    @Test
    @DisplayName("shouldReturnTransactionWhenGatewaySucceeds")
    void shouldReturnTransactionWhenGatewaySucceeds() {
        var tx = txWithRequest(productQuery(DEFAULT_ID));
        when(gateway.getProductDetail(any()))
                .thenAnswer(invocation -> Mono.just((Transaction) invocation.getArgument(0)));

        var useCase = new RetrieveProductDetailUseCase(gateway);

        StepVerifier.create(useCase.retrieve(tx))
                .expectNext(tx)
                .verifyComplete();
    }

    @Test
    @DisplayName("shouldPropagateErrorWhenGatewayFails")
    void shouldPropagateErrorWhenGatewayFails() {
        var tx = txWithRequest(productQuery(DEFAULT_ID));
        when(gateway.getProductDetail(any()))
                .thenReturn(Mono.error(new IllegalStateException("boom")));

        var useCase = new RetrieveProductDetailUseCase(gateway);

        StepVerifier.create(useCase.retrieve(tx))
                .expectErrorSatisfies(ex -> {
                    assert ex instanceof IllegalStateException;
                    assert ex.getMessage() != null && ex.getMessage().contains("boom");
                })
                .verify();
    }

}
