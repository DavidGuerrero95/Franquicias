package co.com.nequi.secretsmanager;

import co.com.nequi.commons.exception.TechnicalException;
import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException;

@Component
public class SecretsManager {

    private final AwsSecretsManagerProperties properties;
    private final ObjectMapper mapper;

    public SecretsManager(AwsSecretsManagerProperties properties, ObjectMapper mapper) {
        this.properties = properties;
        this.mapper = mapper;
    }

    public <T> T getSecret(String secretName, Class<T> type) {
        try (SecretsManagerClient client = SecretsManagerClientFactory.build(properties)) {
            var req = GetSecretValueRequest.builder().secretId(secretName).build();
            var res = client.getSecretValue(req);
            return mapper.readValue(res.secretString(), type);
        } catch (ResourceNotFoundException e) {
            throw new TechnicalException(e, TechnicalExceptionEnum.SECRET_NOT_FOUND);
        } catch (Exception e) {
            throw new TechnicalException(e, TechnicalExceptionEnum.SECRET_READ_ERROR);
        }
    }

    public DbSecret getDbSecret() {
        return getSecret(properties.secretName(), DbSecret.class);
    }
}