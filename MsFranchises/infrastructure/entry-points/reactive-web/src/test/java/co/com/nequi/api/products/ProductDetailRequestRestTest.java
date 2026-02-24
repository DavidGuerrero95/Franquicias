package co.com.nequi.api.products;

import co.com.nequi.api.commons.exception.ExceptionHandler;
import co.com.nequi.api.commons.util.RequestUtil;
import co.com.nequi.api.config.TestSecurityConfig;
import co.com.nequi.api.config.WebFilterConfiguration;
import co.com.nequi.api.config.filter.HeaderValidationFilter;
import co.com.nequi.api.products.datatest.TestDataProductDetail;
import co.com.nequi.api.products.mapper.ProductDetailDataMapper;
import co.com.nequi.commons.exception.TechnicalException;
import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.usecase.product.RetrieveProductDetailUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {
        ProductDetailRouterRest.class,
        ProductDetailHandler.class,
        RequestUtil.class,
        ExceptionHandler.class,
        WebFilterConfiguration.class,
        ProductDetailRequestRestTest.TestPropsConfig.class,
        TestSecurityConfig.class
})
class ProductDetailRequestRestTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ApplicationContext context;

    @MockitoBean
    private RetrieveProductDetailUseCase useCase;

    @MockitoBean
    private ProductDetailDataMapper mapper;

    @Autowired
    private ProductDetailProperties properties;

    @BeforeEach
    void setup() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void shouldReturnOkWhenRetrieveProductDetailSucceeds() {
        when(useCase.retrieve(any(Transaction.class)))
                .thenReturn(Mono.just(TestDataProductDetail.txWithResponse()));

        when(mapper.toDto(any(Transaction.class)))
                .thenReturn(TestDataProductDetail.expectedRS());

        statusAssertions(properties.productDetail(), TestDataProductDetail.validHeaders())
                .is2xxSuccessful();
    }

    @Test
    void shouldReturnClientErrorWhenPathIsInvalid() {
        statusAssertions("invalid-path").is4xxClientError();
    }

    @Test
    void shouldReturnServerErrorWhenUseCaseFails() {
        when(useCase.retrieve(any(Transaction.class)))
                .thenReturn(Mono.error(new RuntimeException("boom")));

        statusAssertions(properties.productDetail(), TestDataProductDetail.validHeaders())
                .is5xxServerError();
    }

    @Test
    void shouldReturnServerErrorWhenTechnicalExceptionOccurs() {
        when(useCase.retrieve(any(Transaction.class))).thenReturn(
                Mono.error(new TechnicalException(
                        new Throwable(TechnicalExceptionEnum.TECHNICAL_SERVER_ERROR.getDescription()),
                        TechnicalExceptionEnum.TECHNICAL_SERVER_ERROR)));

        statusAssertions(properties.productDetail(), TestDataProductDetail.validHeaders())
                .is5xxServerError();
    }

    @Test
    void shouldReturnClientErrorWhenHeadersAreIncomplete() {
        Map<String, String> incompleteHeaders = Map.of("message-id", "hdjhseuiuqwqyw");

        statusAssertions(properties.productDetail(), incompleteHeaders)
                .is4xxClientError();
    }

    private StatusAssertions statusAssertions(String path, Map<String, String> headers) {
        return client.post()
                .uri(uriBuilder -> uriBuilder
                        .path(properties.consultPathBase().concat(path))
                        .build())
                .headers(h -> h.setAll(headers))
                .bodyValue(TestDataProductDetail.validRQ())
                .exchange()
                .expectStatus();
    }

    private StatusAssertions statusAssertions(String path) {
        return client.post()
                .uri(properties.consultPathBase() + path)
                .headers(h -> h.setAll(TestDataProductDetail.validHeaders()))
                .bodyValue(TestDataProductDetail.validRQ())
                .exchange()
                .expectStatus();
    }

    @TestConfiguration
    @EnableConfigurationProperties({ProductDetailProperties.class, HeaderValidationFilter.class})
    static class TestPropsConfig {
        // Solo habilita el binding de @ConfigurationProperties en el slice de prueba.
    }
}
