package co.com.nequi.api.logger;

import co.com.nequi.log.LogConstants;
import co.com.nequi.log.LogWriter;
import co.com.nequi.log.TechMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ResponseLoggingFilter implements WebFilter {

    private static final int MAX_LOG_BODY_SIZE = 5000;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                MediaType ct = getHeaders().getContentType();
                if (ct == null || !(MediaType.APPLICATION_JSON.isCompatibleWith(ct) || MediaType.TEXT_HTML.isCompatibleWith(ct))) {
                    return super.writeWith(body);
                }
                return Flux.from(body)
                        .flatMap(this::processResponse)
                        .as(super::writeWith);
            }

            private Mono<DataBuffer> processResponse(DataBuffer dataBuffer) {
                return Mono.fromCallable(() -> {
                            var content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);

                            var responseBody = new String(content, StandardCharsets.UTF_8);
                            logResponse(exchange, responseBody);

                            return bufferFactory().wrap(content);
                        })
                        .subscribeOn(Schedulers.boundedElastic());
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }


    private void logResponse(ServerWebExchange exchange, String responseBody) {
        HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
        String route = exchange.getRequest().getPath().value();
        String truncatedBody = truncateBody(responseBody);

        Map<String, Serializable> params = new HashMap<>();
        params.put(LogConstants.URL.getName(), route);
        params.put(LogConstants.HEADERS.getName().concat(LogConstants.RESPONSE.getName()), responseHeaders);
        params.put(LogConstants.END_NAME.getName(), System.currentTimeMillis());

        LogWriter.info(TechMessage.buildTechMessage(
                null,
                parseJsonSafeSync(truncatedBody),
                params,
                this.getClass().getSimpleName()
        ));
    }

    private String truncateBody(String body) {
        if (body.length() > MAX_LOG_BODY_SIZE) {
            return body.substring(0, MAX_LOG_BODY_SIZE) + "... [TRUNCATED]";
        }
        return body;
    }

    private Object parseJsonSafeSync(String json) {
        try {
            return objectMapper.readValue(json, Object.class);
        } catch (JsonProcessingException e) {
            return json;
        }
    }

}
