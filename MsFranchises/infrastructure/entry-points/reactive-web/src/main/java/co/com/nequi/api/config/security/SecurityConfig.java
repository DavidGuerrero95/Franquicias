package co.com.nequi.api.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityConfigProperties properties;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        String base = properties.getConsultPathBase();
        if (!base.endsWith("/")) base = base + "/";
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.cors(Customizer.withDefaults());
        http.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);
        http.formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        String finalBase = base;
        http.authorizeExchange(ex -> ex
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .pathMatchers(finalBase + "health", finalBase + "health/**").permitAll()
                .pathMatchers(finalBase + "swagger.html").permitAll()
                .pathMatchers(finalBase + "swagger-ui/**").permitAll()
                .pathMatchers(finalBase + "api-docs", finalBase + "api-docs/**").permitAll()
                .pathMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
                .pathMatchers("/v3/api-docs/**").permitAll()
                .pathMatchers("/webjars/**").permitAll()
                .pathMatchers("/favicon.ico").permitAll()
                .pathMatchers("/.well-known/**").permitAll()
                .pathMatchers(finalBase + "franchises/**").permitAll()
                .anyExchange().denyAll()
        );
        return http.build();
    }

}