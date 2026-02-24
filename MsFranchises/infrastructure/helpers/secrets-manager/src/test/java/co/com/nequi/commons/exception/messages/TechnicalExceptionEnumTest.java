package co.com.nequi.commons.exception.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TechnicalExceptionEnumTest {

    @Test
    void allValuesShouldHaveNonEmptyFields() {
        for (var v : TechnicalExceptionEnum.values()) {
            assertNotNull(v.getCode(), () -> "code null for " + v.name());
            assertFalse(v.getCode().isBlank(), () -> "code blank for " + v.name());
            assertNotNull(v.getDescription(), () -> "description null for " + v.name());
            assertFalse(v.getDescription().isBlank(), () -> "description blank for " + v.name());
            assertNotNull(v.getMessage(), () -> "message null for " + v.name());
        }
    }
}