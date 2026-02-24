package co.com.nequi.api.commons.util;

import co.com.nequi.commons.constants.Constants;
import co.com.nequi.commons.error.ErrorRes;
import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.log.LogWriter;
import co.com.nequi.log.TechMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestUtil {
    private final Validator validator;

    public <T> Mono<T> checkBodyRequest(T dto, ServerRequest serverRequest) {
        return Mono.just(dto)
                .map(validator::validate)
                .doOnNext(response -> getLog(response, serverRequest))
                .map(Set::isEmpty)
                .filter(valid -> valid)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessExceptionEnum.REQUEST_BODY)))
                .thenReturn(dto);
    }

    private void getLog(Set<? extends ConstraintViolation<?>> constraintViolations, ServerRequest serverRequest) {
        if (!constraintViolations.isEmpty()) {
            printLog(constraintViolations, serverRequest);
        }
    }

    private void printLog(Set<? extends ConstraintViolation<?>> constraintViolations, ServerRequest serverRequest) {
        String message = constraintViolations.stream()
                .map(cv -> cv.getPropertyPath() + Constants.COLON_SEPARATOR + cv.getMessage())
                .collect(Collectors.joining(Constants.COMMA_SEPARATOR));

        var error = ErrorRes.builder()
                .errors(List.of(
                        ErrorRes.Data.builder()
                                .type(Constants.VALIDATION_ERROR)
                                .reason(BusinessExceptionEnum.REQUEST_BODY.getMessage())
                                .domain(serverRequest.path())
                                .code(BusinessExceptionEnum.REQUEST_BODY.getCode())
                                .message(message)
                                .build()
                ))
                .build();

        LogWriter.error(TechMessage.buildTechMessageError(
                new BusinessException(error), serverRequest,
                RequestUtil.class.getName()
        ));
    }

    public Mono<String> validateHeader(String headerName, String headerValue, Pattern pattern) {
        if (headerValue != null && !headerValue.isEmpty() && !pattern.matcher(headerValue).matches()) {
            LogWriter.error("Header validation failed for header: " + headerName
                    + " with value: " + headerValue + ". Expected pattern: " + pattern.pattern());
            return Mono.error(new BusinessException(BusinessExceptionEnum.REQUEST_BODY));
        }
        return Mono.justOrEmpty(headerValue);
    }

}
