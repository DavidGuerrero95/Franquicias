package co.com.nequi.api.franchise;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "entry-points.reactive-web")
public record FranchiseRoutesProperties(
        String consultPathBase,
        String franchiseCreate,
        String franchiseRename,
        String topStockByFranchise
) { }