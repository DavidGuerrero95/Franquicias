package co.com.nequi.log.configuration;

import co.com.nequi.log.LogWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class LoggerConfiguration {
    private static final String REGEX = ",";
    private final String maskedFields;
    private final ObjectMapper objectMapper;

    public LoggerConfiguration(@Value("${logging.masked-fields}") String maskedFields,
                               ObjectMapper objectMapper) {
        this.maskedFields = maskedFields;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void configureJsonLogger() {
        Set<String> fields = Arrays.stream(maskedFields.split(REGEX))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toUnmodifiableSet());

        LogWriter.configure(objectMapper, fields);
    }
}