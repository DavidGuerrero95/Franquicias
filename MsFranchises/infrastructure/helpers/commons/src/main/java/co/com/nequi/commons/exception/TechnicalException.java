package co.com.nequi.commons.exception;

import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {

    private final TechnicalExceptionEnum exception;

    public TechnicalException(Throwable throwable, TechnicalExceptionEnum technicalExceptionMessage) {
        super(throwable);
        this.exception = technicalExceptionMessage;
    }

    public TechnicalException(String message, TechnicalExceptionEnum technicalExceptionMessage) {
        super(message);
        this.exception = technicalExceptionMessage;
    }

    public TechnicalException(TechnicalExceptionEnum technicalExceptionMessage) {
        super(technicalExceptionMessage.getMessage());
        this.exception = technicalExceptionMessage;
    }
}