package co.com.nequi.secretsmanager;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.secrets-manager")
public record AwsSecretsManagerProperties(
        String region,
        String secretName,
        String endpoint,
        String accessKeyId,
        String secretAccessKey
) { }