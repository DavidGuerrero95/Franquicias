package co.com.nequi.r2dbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "mysql.pool")
public record MySQLConnectionProperties(
        int initialSize,
        int maxSize,
        Duration maxIdleTime,
        Duration maxLifeTime,
        int acquireRetry
) {
}