package co.com.nequi.commons.exception;

import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class TechnicalExceptionTest {

    @Test
    void ctorWithThrowableAndEnumShouldStoreEnum() {
        var cause = new RuntimeException("root");
        var ex = new TechnicalException(cause, TechnicalExceptionEnum.UNEXPECTED_EXCEPTION);
        assertEquals(TechnicalExceptionEnum.UNEXPECTED_EXCEPTION, ex.getException());
        assertSame(cause, ex.getCause());
    }

    @Test
    void ctorWithMessageAndEnumShouldStoreEnum() {
        var ex = new TechnicalException("x", TechnicalExceptionEnum.INVALID_REQUEST);
        assertEquals("x", ex.getMessage());
        assertEquals(TechnicalExceptionEnum.INVALID_REQUEST, ex.getException());
    }

    @Test
    void ctorWithEnumShouldUseEnumMessage() {
        var ex = new TechnicalException(TechnicalExceptionEnum.TECHNICAL_SERVER_ERROR);
        assertEquals(TechnicalExceptionEnum.TECHNICAL_SERVER_ERROR.getMessage(), ex.getMessage());
        assertEquals(TechnicalExceptionEnum.TECHNICAL_SERVER_ERROR, ex.getException());
    }
}