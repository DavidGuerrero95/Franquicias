package co.com.nequi.r2dbc.config;

import co.com.nequi.commons.exception.TechnicalException;
import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;
import co.com.nequi.secretsmanager.DbSecret;
import co.com.nequi.secretsmanager.SecretsManager;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories(basePackages = "co.com.nequi.r2dbc")
public class MySQLConnectionPool {

    @Bean
    public ConnectionFactory connectionFactory(SecretsManager secretsManager, MySQLConnectionProperties props) {
        try {
            DbSecret secret = secretsManager.getDbSecret();

            var options = ConnectionFactoryOptions.builder()
                    .option(DRIVER, "mysql")
                    .option(HOST, secret.host())
                    .option(PORT, secret.port())
                    .option(USER, secret.username())
                    .option(PASSWORD, secret.password())
                    .option(DATABASE, secret.dbname())
                    .build();

            ConnectionFactory baseFactory = ConnectionFactories.get(options);

            ConnectionPoolConfiguration poolConfig = ConnectionPoolConfiguration.builder(baseFactory)
                    .initialSize(props.initialSize())
                    .maxSize(props.maxSize())
                    .maxIdleTime(nonNull(props.maxIdleTime(), Duration.ofMinutes(30)))
                    .maxLifeTime(nonNull(props.maxLifeTime(), Duration.ofHours(1)))
                    .acquireRetry(props.acquireRetry())
                    .build();

            return new ConnectionPool(poolConfig);
        } catch (Exception e) {
            throw new TechnicalException(e, TechnicalExceptionEnum.DB_CONNECTION_ERROR);
        }
    }

    private static Duration nonNull(Duration value, Duration fallback) {
        return value == null ? fallback : value;
    }
}