package co.com.nequi.commons.exception;

import co.com.nequi.commons.error.ErrorRes;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final BusinessExceptionEnum businessExceptionEnum;
    private final transient ErrorRes error;

    public BusinessException(BusinessExceptionEnum businessExceptionEnum) {
        super(businessExceptionEnum.getMessage());
        this.error = new ErrorRes();
        this.businessExceptionEnum = businessExceptionEnum;
    }

    public BusinessException(ErrorRes error) {
        this.businessExceptionEnum = null;
        this.error = error;
    }

}