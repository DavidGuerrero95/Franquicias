package co.com.nequi.commons.exception.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void builderShouldSetAllFields() {
        var er = ResponseTestData.sampleErrorResponse();
        assertEquals("E1", er.getCode());
        assertEquals("msg", er.getMessage());
        assertEquals("/dom", er.getDomain());
        assertEquals("why", er.getReason());
        assertEquals("T", er.getType());
    }

    @Test
    void settersAndGettersShouldWork() {
        var er = new ErrorResponse();
        er.setCode("C");
        er.setMessage("M");
        er.setDomain("/d");
        er.setReason("R");
        er.setType("T");

        assertAll(
                () -> assertEquals("C", er.getCode()),
                () -> assertEquals("M", er.getMessage()),
                () -> assertEquals("/d", er.getDomain()),
                () -> assertEquals("R", er.getReason()),
                () -> assertEquals("T", er.getType())
        );
    }
}