package co.com.nequi.api.config.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HeaderValidationFilterTest {

    private HeaderValidationFilter headerValidationFilter;
    private WebFilterChain filterChain;

    @BeforeEach
    void setUp() {
        HeaderValidationFilter.RequiredHeaders requiredHeaders = HeaderValidationFilter.RequiredHeaders.builder()
                .general(Arrays.asList("message-id", "channel", "aid-creator", "content-type", "accept"))
                .build();
        headerValidationFilter = new HeaderValidationFilter();
        headerValidationFilter.setRequiredHeaders(requiredHeaders);

        filterChain = Mockito.mock(WebFilterChain.class);
    }

    @Test
    void filterWhenAllHeadersArePresentShouldProceed() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("message-id", "12345")
                .header("channel", "web")
                .header("aid-creator", "creator")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        when(filterChain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        Mono<Void> result = headerValidationFilter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .verifyComplete();

        verify(filterChain, times(1)).filter(exchange);
    }

    @Test
    void filterWhenHeadersAreMissingShouldThrowResponseStatusException() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("message-id", "12345")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);


        Mono<Void> resultMono = headerValidationFilter.filter(exchange, filterChain);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, resultMono::block);

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Missing headers: [channel, aid-creator, content-type, accept]", exception.getReason());
    }

    @Test
    void filterWhenHeadersAreBlankShouldThrowResponseStatusException() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("message-id", " ")
                .header("channel", "web")
                .header("aid-creator", "creator")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        Mono<Void> resultMono = headerValidationFilter.filter(exchange, filterChain);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, resultMono::block);

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Missing headers: [message-id]", exception.getReason());
    }

    @Test
    void filterWhenNoRequiredHeadersConfiguredShouldProceed() {
        headerValidationFilter.setRequiredHeaders(HeaderValidationFilter.RequiredHeaders.builder()
                .general(Collections.emptyList())
                .build());

        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        when(filterChain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        Mono<Void> result = headerValidationFilter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .verifyComplete();

        verify(filterChain, times(1)).filter(exchange);
    }

    @Test
    void filterWhenSkipFilterAttributeIsTrueShouldSkipValidation() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        exchange.getAttributes().put("SKIP_FILTER", Boolean.TRUE);

        when(filterChain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        Mono<Void> result = headerValidationFilter.filter(exchange, filterChain);

        StepVerifier.create(result)
                .verifyComplete();

        verify(filterChain, times(1)).filter(exchange);
    }
}
