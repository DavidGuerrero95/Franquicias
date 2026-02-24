package co.com.nequi.api.config;

import co.com.nequi.api.config.filter.HeaderValidationFilter;
import co.com.nequi.api.config.filter.HealthCheckFilter;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WebFilterConfigurationTest {

    @Test
    void testHealthCheckFilterBeanCreation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebFilterConfiguration.class);
        HealthCheckFilter healthCheckFilter = context.getBean(HealthCheckFilter.class);
        assertNotNull(healthCheckFilter);
    }

    @Test
    void testHeaderValidationFilterBeanCreation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebFilterConfiguration.class);
        HeaderValidationFilter headerValidationFilter = context.getBean("headerValidationFilter", HeaderValidationFilter.class);
        assertNotNull(headerValidationFilter);
    }
}
