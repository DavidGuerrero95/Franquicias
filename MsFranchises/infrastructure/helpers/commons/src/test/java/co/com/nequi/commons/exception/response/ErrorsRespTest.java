package co.com.nequi.commons.exception.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ErrorsRespTest {

    @Test
    void shouldHoldListOfErrorResponse() {
        var errs = ResponseTestData.sampleErrorsResp();
        assertNotNull(errs.getErrors());
        assertEquals(1, errs.getErrors().size());
        assertEquals("E1", errs.getErrors().getFirst().getCode());
    }

    @Test
    void settersShouldReplaceList() {
        var errs = new ErrorsResp();
        assertNull(errs.getErrors());
        errs.setErrors(ResponseTestData.sampleErrorsResp().getErrors());
        assertNotNull(errs.getErrors());
        assertEquals(1, errs.getErrors().size());
    }
}