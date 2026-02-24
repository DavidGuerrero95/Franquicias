package co.com.nequi.api.config;

import co.com.nequi.api.commons.exception.ExceptionHandler;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;

public class BeanConfig {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public ExceptionHandler exceptionHandler(ApplicationContext applicationContext,
                                             ErrorAttributes errorAttributes,
                                             ServerCodecConfigurer serverCodecConfigurer) {
        return new ExceptionHandler(errorAttributes, applicationContext, serverCodecConfigurer);
    }

}
