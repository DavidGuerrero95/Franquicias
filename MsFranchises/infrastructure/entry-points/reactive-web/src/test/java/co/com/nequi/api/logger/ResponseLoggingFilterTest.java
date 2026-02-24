package co.com.nequi.api.logger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResponseLoggingFilterTest {

    private final DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
    private ResponseLoggingFilter filter;
    private MockServerWebExchange exchange;
    private WebFilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new ResponseLoggingFilter();
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").build());
        chain = mock(WebFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());
    }

    private Mono<Void> runFilter(String body) {
        MockServerWebExchange ex =
                MockServerWebExchange.from(MockServerHttpRequest.get("/route").build());

        ArgumentCaptor<ServerWebExchange> cap = ArgumentCaptor.forClass(ServerWebExchange.class);
        when(chain.filter(cap.capture())).thenAnswer(inv -> {
            ServerWebExchange mutated = cap.getValue();
            DataBuffer buf = bufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8));
            return mutated.getResponse().writeWith(Flux.just(buf));
        });

        return filter.filter(ex, chain);
    }

    @Test
    void shouldHandleLargeResponseBodyWithTruncation() {
        String responseBody = "A".repeat(6000);
        DataBuffer dataBuffer = bufferFactory.wrap(responseBody.getBytes(StandardCharsets.UTF_8));

        exchange.getResponse().writeWith(Flux.just(dataBuffer));

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        exchange.getAttributes().put("transactionId", "12345");

    }

    @Test
    void shouldHandleInvalidJsonInResponseBody() {
        String invalidJson = "invalid-json";
        DataBuffer dataBuffer = bufferFactory.wrap(invalidJson.getBytes(StandardCharsets.UTF_8));

        exchange.getResponse().writeWith(Flux.just(dataBuffer));

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        exchange.getAttributes().put("transactionId", "12345");

    }

    @Test
    void shouldHandleEmptyResponseBody() {
        DataBuffer dataBuffer = bufferFactory.wrap("".getBytes(StandardCharsets.UTF_8));

        exchange.getResponse().writeWith(Flux.just(dataBuffer));

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        exchange.getAttributes().put("transactionId", "12345");

    }

    @Test
    void jsonBody() {
        String json = "{\"key\":42}";
        StepVerifier.create(runFilter(json)).verifyComplete();
    }

    @Test
    void hugeBody() {
        String large = "x".repeat(6_000);
        StepVerifier.create(runFilter(large)).verifyComplete();
    }

    @Test
    void invalidJson() {
        StepVerifier.create(runFilter("not-json")).verifyComplete();
    }

    @Test
    void truncateBodyHelper() throws Exception {
        Method m = ResponseLoggingFilter.class
                .getDeclaredMethod("truncateBody", String.class);
        m.setAccessible(true);

        String shortTxt = "abc";
        String huge = "y".repeat(5_001);

        String r1 = (String) m.invoke(filter, shortTxt);
        String r2 = (String) m.invoke(filter, huge);

        Assertions.assertThat(r1).isEqualTo(shortTxt);
        Assertions.assertThat(r2).endsWith("... [TRUNCATED]").hasSize(5_000 + 15);
    }

    @Test
    void parseJsonHelper() throws Exception {
        Method m = ResponseLoggingFilter.class
                .getDeclaredMethod("parseJsonSafeSync", String.class);
        m.setAccessible(true);

        Object good = m.invoke(filter, "{\"a\":1}");
        Object bad = m.invoke(filter, "oops");

        Assertions.assertThat(good).isInstanceOfAny(java.util.Map.class);
        Assertions.assertThat(bad).isEqualTo("oops");
    }

}