package co.com.nequi.commons.exception;

import co.com.nequi.commons.error.ErrorRes;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ServerExceptionTest {

    @Test
    void messageShouldFallbackWhenNoError() {
        var ex = new ServerException(null);
        assertEquals(ServerException.DEFAULT_ERROR_MESSAGE, ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
        assertNull(ex.getError());
    }

    @Test
    void messageShouldUseFirstErrorMessage() {
        ErrorRes er = ExceptionTestData.errorResWithMessage("S1", "Server boom");
        var ex = new ServerException(er);
        assertEquals("Server boom", ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
        assertSame(er, ex.getError());
    }

    @Test
    void statusShouldDefaultWhenNullProvided() {
        ErrorRes er = ExceptionTestData.errorResWithMessage("S2", "msg");
        var ex = new ServerException(er, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    @Test
    void statusShouldBeCustomWhenGiven() {
        ErrorRes er = ExceptionTestData.errorResWithMessage("S3", "msg");
        var ex = new ServerException(er, HttpStatus.SERVICE_UNAVAILABLE);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ex.getStatus());
    }
}