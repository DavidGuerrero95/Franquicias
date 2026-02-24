package co.com.nequi.api.branch;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "entry-points.reactive-web")
public record BranchRoutesProperties(
        String consultPathBase,
        String branchCreate,
        String branchRename
) { }