package co.com.nequi.commons.exception.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BusinessExceptionEnumTest {

    @Test
    void allValuesShouldHaveNonEmptyFields() {
        for (var v : BusinessExceptionEnum.values()) {
            assertNotNull(v.getCode());
            assertFalse(v.getCode().isBlank());
            assertNotNull(v.getDescription());
            assertFalse(v.getDescription().isBlank());
            assertNotNull(v.getMessage());
            assertFalse(v.getMessage().isBlank());
        }
    }
}