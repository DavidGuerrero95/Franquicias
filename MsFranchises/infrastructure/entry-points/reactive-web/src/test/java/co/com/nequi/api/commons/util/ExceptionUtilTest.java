package co.com.nequi.api.commons.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ExceptionUtilTest {

    @Test
    void errorFromExceptionTest() {
        Assertions.assertNotNull(ExceptionUtil.errorFromException(new Throwable("prueba unitaria"),
                "/detained-payment-files"));
    }

    @Test
    void fromResponseStatusTest() {
        Assertions.assertNotNull(ExceptionUtil.fromResponseStatus("/change-status"));
    }

    @Test
    void getErrorMonoTest() {
        Assertions.assertNotNull(ExceptionUtil.getErrorMono("/detained-payment-files",
                "ERROR EN PARAMETROS DE ENTRADA"));
        Assertions.assertNotNull(ExceptionUtil.getErrorMono("/detained-payment-files",
                "ERROR EN HEADERS"));
        Assertions.assertNotNull(ExceptionUtil.getErrorMono("/detained-payment-files",
                "OTRO ERROR"));
    }

    @Test
    void getReasonTest() {
        ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Assertions.assertNotNull(ExceptionUtil.getReason(responseStatusException));
    }

}