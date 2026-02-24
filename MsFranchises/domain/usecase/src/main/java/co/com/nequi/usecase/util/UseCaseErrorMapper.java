package co.com.nequi.usecase.util;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.TechnicalException;
import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;

public final class UseCaseErrorMapper {
    private UseCaseErrorMapper() { }

    public static Throwable map(Throwable error) {
        if (error instanceof BusinessException || error instanceof TechnicalException) {
            return error;
        }
        return new TechnicalException(error, TechnicalExceptionEnum.UNEXPECTED_EXCEPTION);
    }
}