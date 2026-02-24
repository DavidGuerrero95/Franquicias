package co.com.nequi.commons.exception;

import co.com.nequi.commons.error.ErrorRes;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class BusinessExceptionTest {

    @Test
    void shouldExposeEnumAndMessageConstructor() {
        var ex = new BusinessException(BusinessExceptionEnum.REQUEST_BODY);
        assertEquals(BusinessExceptionEnum.REQUEST_BODY, ex.getBusinessExceptionEnum());
        assertNotNull(ex.getMessage());
        assertNotNull(ex.getError());
    }

    @Test
    void shouldStoreProvidedError() {
        ErrorRes er = ExceptionTestData.errorResWithMessage("C", "M");
        var ex = new BusinessException(er);
        assertNull(ex.getBusinessExceptionEnum());
        assertSame(er, ex.getError());
    }
}