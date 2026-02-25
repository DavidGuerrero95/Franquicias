package co.com.nequi.api.product;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "entry-points.reactive-web")
public record ProductRoutesProperties(
        String consultPathBase,
        String productCreate,
        String productDelete,
        String productStockUpdate,
        String productRename
) {
}