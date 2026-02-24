package co.com.nequi.api.config;

import co.com.nequi.api.config.filter.HeaderValidationFilter;
import co.com.nequi.api.config.filter.HealthCheckFilter;
import co.com.nequi.api.logger.RequestLoggingFilter;
import co.com.nequi.api.logger.ResponseLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class WebFilterConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public HealthCheckFilter healthCheckFilter() {
        return new HealthCheckFilter();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public HeaderValidationFilter headerValidationFilter() {
        return new HeaderValidationFilter();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 2)
    public RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    public ResponseLoggingFilter responseLoggingFilter() {
        return new ResponseLoggingFilter();
    }
}

