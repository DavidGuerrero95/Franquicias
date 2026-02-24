package co.com.nequi.api.logger;

import co.com.nequi.log.LogWriter;
import co.com.nequi.log.TechMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;

import static co.com.nequi.api.logger.LogConstant.CACHE_REQUEST_BODY;

@Component
@RequiredArgsConstructor
public class RequestLoggingFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .defaultIfEmpty(exchange.getResponse().bufferFactory().wrap(new byte[0]))
                .flatMap(dataBuffer -> logRequest(exchange, dataBuffer))
                .flatMap(bytes -> getChainFilter(exchange, chain, bytes));
    }

    private Mono<byte[]> logRequest(ServerWebExchange exchange, DataBuffer dataBuffer) {
        return Mono.fromCallable(() -> {
                    var bodyBytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bodyBytes);
                    DataBufferUtils.release(dataBuffer);

                    String body = new String(bodyBytes, StandardCharsets.UTF_8).trim();
                    exchange.getAttributes().put(CACHE_REQUEST_BODY, body);
                    LogWriter.info(TechMessage.buildTechMessage(exchange, this.getClass().getSimpleName()));
                    return bodyBytes;
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    protected Mono<Void> getChainFilter(ServerWebExchange exchange, WebFilterChain chain, byte[] bytes) {
        ServerHttpRequest modifiedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return Flux.just(exchange.getResponse().bufferFactory().wrap(bytes));
            }
        };
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }
}
