package co.com.nequi.commons.error;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.TechnicalException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;

import java.util.List;

public final class ErrorTestData {
    private ErrorTestData() {
    }

    public static ErrorRes oneError(String code, String message, String domain, String reason) {
        return ErrorRes.builder()
                .errors(List.of(ErrorRes.Data.builder()
                        .code(code)
                        .message(message)
                        .domain(domain)
                        .reason(reason)
                        .build()))
                .build();
    }

    public static ErrorRes emptyErrorsObject() {
        return new ErrorRes();
    }

    public static BusinessException businessWithEnum(BusinessExceptionEnum e) {
        return new BusinessException(e);
    }

    public static BusinessException businessWithError(ErrorRes er) {
        return new BusinessException(er);
    }

    public static TechnicalException technicalWith(TechnicalExceptionEnum e) {
        return new TechnicalException("boom", e);
    }
}