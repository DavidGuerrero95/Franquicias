package co.com.nequi.api.commons;

import co.com.nequi.api.config.filter.HeaderValidationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BeanConfiguration {

    @Bean(name = "headerValidationFilter")
    public HeaderValidationFilter headerValidationFilter() {
        var filter = new HeaderValidationFilter();
        filter.setRequiredHeaders(HeaderValidationFilter.RequiredHeaders.builder()
                        .general(List.of("message-id", "accept", "content-type"))
                .build());
        return filter;
    }

}