package co.com.nequi.api.products;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "entry-points.reactive-web")
public record ProductDetailProperties(
        String consultPathBase,
        String productDetail
) { }