package co.com.nequi.api.config.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Getter
@ConfigurationProperties(prefix = "entry-points.reactive-web")
@NoArgsConstructor
public class HeaderValidationFilter implements WebFilter {

    private static final String SKIP_FILTER_ATTRIBUTE = "SKIP_FILTER";
    private RequiredHeaders requiredHeaders = RequiredHeaders.builder().general(List.of()).build();

    private String consultPathBase;

    public void setConsultPathBase(String consultPathBase) {
        this.consultPathBase = consultPathBase;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }
        if (Boolean.TRUE.equals(exchange.getAttribute(SKIP_FILTER_ATTRIBUTE))) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getPath().value();
        String base = normalizeBase(consultPathBase);
        if (!base.isEmpty() && !path.startsWith(base)) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = exchange.getRequest().getHeaders();
        List<String> missingHeaders = requiredHeaders.getGeneral().stream()
                .filter(header -> !headers.containsKey(header) ||
                        Objects.requireNonNull(headers.getFirst(header)).isBlank())
                .toList();

        if (!missingHeaders.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Missing headers: " + missingHeaders));
        }

        exchange.getAttributes().put("validatedHeaders", headers.toSingleValueMap());
        return chain.filter(exchange);
    }

    private String normalizeBase(String base) {
        if (base == null) return "";
        return base.endsWith("/") ? base : base + "/";
    }

    public void setRequiredHeaders(RequiredHeaders requiredHeaders) {
        this.requiredHeaders = requiredHeaders;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequiredHeaders {
        private List<String> general;
    }

}
