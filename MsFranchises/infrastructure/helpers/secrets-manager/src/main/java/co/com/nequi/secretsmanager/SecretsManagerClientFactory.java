package co.com.nequi.secretsmanager;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import java.net.URI;

public final class SecretsManagerClientFactory {

    private SecretsManagerClientFactory() { }

    public static SecretsManagerClient build(AwsSecretsManagerProperties props) {
        var builder = SecretsManagerClient.builder()
                .region(Region.of(props.region()));

        if (props.endpoint() != null && !props.endpoint().isBlank()) {
            builder = builder
                    .endpointOverride(URI.create(props.endpoint()))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(props.accessKeyId(), props.secretAccessKey())
                    ));
        }

        return builder.build();
    }
}