package co.com.nequi.api.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "entry-points.reactive-web")
public class SecurityConfigProperties {

    private String consultPathBase;

}
