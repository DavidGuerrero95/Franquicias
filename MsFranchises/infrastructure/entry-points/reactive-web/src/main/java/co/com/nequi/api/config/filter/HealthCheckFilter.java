package co.com.nequi.api.config.filter;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

public class HealthCheckFilter implements WebFilter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final String SKIP_FILTER_ATTRIBUTE = "SKIP_FILTER";

    private static final List<String> IGNORE_PATHS = List.of(
            "/**/health", "/**/health/**",
            "/**/swagger.html",
            "/**/swagger-ui.html", "/**/swagger-ui/**",
            "/**/api-docs", "/**/api-docs/**",
            "/v3/api-docs", "/v3/api-docs/**",
            "/webjars/**",
            "/favicon.ico", "/**/favicon.ico",
            "/.well-known/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }
        return Mono.just(exchange.getRequest().getURI().getPath())
                .filter(this::shouldSkipFilter)
                .doOnNext(path -> exchange.getAttributes().put(SKIP_FILTER_ATTRIBUTE, Boolean.TRUE))
                .then(chain.filter(exchange));
    }

    private boolean shouldSkipFilter(String requestPath) {
        return IGNORE_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }
}

