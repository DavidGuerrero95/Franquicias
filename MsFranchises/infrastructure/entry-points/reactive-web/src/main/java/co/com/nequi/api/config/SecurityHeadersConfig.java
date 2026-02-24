package co.com.nequi.api.config;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SecurityHeadersConfig implements WebFilter {

    private static final AntPathMatcher matcher = new AntPathMatcher();

    private static final List<String> SWAGGER_PATHS = List.of(
            "/**/swagger.html",
            "/**/swagger-ui.html",
            "/**/swagger-ui/**",
            "/**/api-docs",
            "/**/api-docs/**",
            "/v3/api-docs",
            "/v3/api-docs/**"
    );

    private static boolean isSwaggerPath(String path) {
        return SWAGGER_PATHS.stream().anyMatch(p -> matcher.match(p, path));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var headers = exchange.getResponse().getHeaders();
        var path = exchange.getRequest().getURI().getPath();

        headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        headers.add(HttpHeaders.CACHE_CONTROL, "private, no-cache, no-store, must-revalidate");
        headers.add("X-Permitted-Cross-Domain-Policies", "master-only");
        headers.add("Expires", "0");
        headers.add("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        headers.add("Referrer-Policy", "strict-origin-when-cross-origin");
        headers.add("X-Content-Type-Options", "nosniff");
        headers.add("Permissions-Policy", "geolocation=(self), microphone=()");
        if (isSwaggerPath(path)) {
            headers.add("Content-Security-Policy",
                    "default-src 'self'; " +
                            "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                            "style-src 'self' 'unsafe-inline'; " +
                            "img-src 'self' data: blob:; " +
                            "font-src 'self' data:; " +
                            "connect-src 'self'; " +
                            "worker-src 'self' blob:; " +
                            "frame-ancestors 'self'; " +
                            "form-action 'self'");
        } else {
            headers.add("Content-Security-Policy",
                    "default-src 'self'; " +
                            "script-src 'self'; " +
                            "style-src 'self' 'unsafe-inline'; " +
                            "img-src 'self' data:; " +
                            "font-src 'self' data:; " +
                            "connect-src 'self'; " +
                            "worker-src 'self' blob:; " +
                            "frame-ancestors 'self'; " +
                            "form-action 'self'");
        }

        return chain.filter(exchange);
    }
}