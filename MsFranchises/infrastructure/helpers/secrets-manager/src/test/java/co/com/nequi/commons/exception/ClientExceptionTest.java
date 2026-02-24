package co.com.nequi.commons.exception;

import co.com.nequi.commons.error.ErrorRes;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ClientExceptionTest {

    @Test
    void messageShouldFallbackWhenNoError() {
        var ex = new ClientException(null);
        assertEquals(ClientException.DEFAULT_ERROR_MESSAGE, ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertNull(ex.getError());
    }

    @Test
    void messageShouldUseFirstErrorMessage() {
        ErrorRes er = ExceptionTestData.errorResWithMessage("C1", "Client says hi");
        var ex = new ClientException(er);
        assertEquals("Client says hi", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertSame(er, ex.getError());
    }

    @Test
    void statusShouldDefaultWhenNullProvided() {
        ErrorRes er = ExceptionTestData.errorResWithMessage("C2", "msg");
        var ex = new ClientException(er, null);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    void statusShouldBeCustomWhenGiven() {
        ErrorRes er = ExceptionTestData.errorResWithMessage("C3", "msg");
        var ex = new ClientException(er, HttpStatus.UNAUTHORIZED);
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    }
}