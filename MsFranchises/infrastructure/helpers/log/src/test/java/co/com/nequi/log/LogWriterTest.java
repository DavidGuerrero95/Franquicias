package co.com.nequi.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

class LogWriterTest {

    @BeforeAll
    static void setup() {
        LogWriter.configure(new ObjectMapper(), Set.of("password", "authorization", "creditcard"));
    }

    @Test
    @DisplayName("shouldMaskScalarAndContainerAndArrayNodes")
    void shouldMaskScalarAndContainerAndArrayNodes() {
        var payload = Map.of(
                "password", "super-secret",
                "nested", Map.of("authorization", Map.of("token", "123")),
                "array", new Object[]{Map.of("creditCard", "4444-3333-2222-1111")}
        );
        LogWriter.info(payload);
        LogWriter.error(payload);
        LogWriter.debug(payload);
    }
}
