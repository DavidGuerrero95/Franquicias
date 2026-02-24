package co.com.nequi.api.config.filter;

import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HealthCheckFilterTest {

    private final HealthCheckFilter healthCheckFilter = new HealthCheckFilter();
    private final WebFilterChain filterChain = mock(WebFilterChain.class);

    @Test
    void filterWhenHealthPathShouldSetSkipAttribute() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/ms-document-sap/api/v1/health").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(filterChain.filter(exchange)).thenReturn(Mono.empty());

        Mono<Void> result = healthCheckFilter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .verifyComplete();

        verify(filterChain, times(1)).filter(exchange);
        Boolean skipFilter = exchange.getAttribute("SKIP_FILTER");
        assert skipFilter != null && skipFilter;
    }

    @Test
    void filterWhenNonHealthPathShouldNotSetSkipAttribute() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/ms-document-sap/api/v1/other").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(filterChain.filter(exchange)).thenReturn(Mono.empty());

        Mono<Void> result = healthCheckFilter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .verifyComplete();

        verify(filterChain, times(1)).filter(exchange);
        Boolean skipFilter = exchange.getAttribute("SKIP_FILTER");
        assert skipFilter == null;
    }
}
