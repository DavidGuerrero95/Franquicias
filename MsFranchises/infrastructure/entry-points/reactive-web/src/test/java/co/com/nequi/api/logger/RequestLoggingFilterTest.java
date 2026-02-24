package co.com.nequi.api.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class RequestLoggingFilterTest {

    private RequestLoggingFilter filter;

    @BeforeEach
    void setUp() {
        filter = new RequestLoggingFilter();
    }

    @Test
    void shouldDecorateRequestBodyProperly() {
        String body = "{\"key\":\"value\"}";
        MockServerHttpRequest request = MockServerHttpRequest.post("/test-route")
                .header("message-id", "12345")
                .body(body);

        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        WebFilterChain chain = mock(WebFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        String decoratedBody = exchange.getRequest()
                .getBody()
                .map(dataBuffer -> StandardCharsets.UTF_8.decode(
                                dataBuffer.readableByteBuffers().next()
                        ).toString()
                )
                .blockFirst();
        assertEquals(body, decoratedBody);
    }

}
