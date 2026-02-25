package co.com.nequi.secretsmanager;

public record DbSecret(
        String host,
        Integer port,
        String dbname,
        String username,
        String password
) {
}