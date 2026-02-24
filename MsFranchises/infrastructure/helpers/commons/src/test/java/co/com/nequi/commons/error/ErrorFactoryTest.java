package co.com.nequi.commons.error;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ErrorFactoryTest {

    @Test
    void buildErrorTechnicalShouldMapFields() {
        var res = ErrorFactory.buildError(TechnicalExceptionEnum.INVALID_PARAMS, "bad", "/api/x");
        assertNotNull(res);
        assertNotNull(res.getErrors());
        assertEquals(1, res.getErrors().size());
        var e = res.getErrors().getFirst();
        assertEquals(TechnicalExceptionEnum.INVALID_PARAMS.getCode(), e.getCode());
        assertEquals("/api/x", e.getDomain());
        assertEquals("bad", e.getReason());
        assertEquals(TechnicalExceptionEnum.INVALID_PARAMS.getDescription(), e.getMessage());
    }

    @Test
    void buildErrorBusinessShouldMapFields() {
        var res = ErrorFactory.buildError(BusinessExceptionEnum.REQUEST_BODY, "invalid", "/api/y");
        var e = res.getErrors().getFirst();
        assertEquals(BusinessExceptionEnum.REQUEST_BODY.getCode(), e.getCode());
        assertEquals(BusinessExceptionEnum.REQUEST_BODY.getMessage(), e.getMessage());
        assertEquals("invalid", e.getReason());
        assertEquals("/api/y", e.getDomain());
    }

    @Test
    void monoErrorTechnicalShouldEmitOnce() {
        StepVerifier.create(ErrorFactory.monoError(TechnicalExceptionEnum.TECHNICAL_SERVER_ERROR, "x", "/d"))
                .assertNext(err -> {
                    var d = err.getErrors().getFirst();
                    assertEquals(TechnicalExceptionEnum.TECHNICAL_SERVER_ERROR.getCode(), d.getCode());
                    assertEquals("/d", d.getDomain());
                    assertEquals("x", d.getReason());
                })
                .verifyComplete();
    }

    @Test
    void fromBusinessShouldReturnProvidedErrorWhenPresent() {
        var provided = ErrorTestData.oneError("C1", "M1", "/d", "r");
        BusinessException bex = ErrorTestData.businessWithError(provided);
        StepVerifier.create(ErrorFactory.fromBusiness(bex, "/ignored"))
                .assertNext(err -> {
                    assertSame(provided, err);
                    assertEquals("C1", err.getErrors().getFirst().getCode());
                })
                .verifyComplete();
    }

    @Test
    void fromDefaultTechnicalShouldWrapReasonAndDomain() {
        StepVerifier.create(ErrorFactory.fromDefaultTechnical("why", "/k"))
                .assertNext(err -> {
                    var d = err.getErrors().getFirst();
                    assertEquals("why", d.getReason());
                    assertEquals("/k", d.getDomain());
                    assertEquals(TechnicalExceptionEnum.TECHNICAL_SERVER_ERROR.getCode(), d.getCode());
                })
                .verifyComplete();
    }

}
