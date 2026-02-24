package co.com.nequi.api.commons.util;

import co.com.nequi.commons.constants.Constants;
import co.com.nequi.commons.error.ErrorFactory;
import co.com.nequi.commons.error.ErrorRes;
import co.com.nequi.commons.exception.TechnicalException;
import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static reactor.core.publisher.Mono.just;

@UtilityClass
public class ExceptionUtil {

    public Mono<Error> errorFromException(Throwable throwable, String pathService) {
        return Mono.error(throwable)
                .onErrorResume(TechnicalException.class, error -> ErrorFactory.fromTechnical(error, pathService))
                .onErrorResume(ResponseStatusException.class, fromResponseStatus(pathService))
                .onErrorResume(error -> ErrorFactory.fromDefaultTechnical(error.getMessage(), pathService))
                .cast(Error.class);
    }

    public Function<ResponseStatusException, Mono<ErrorRes>> fromResponseStatus(String pathService) {
        return responseStatusException -> getReason(responseStatusException)
                .flatMap(reason -> getErrorMono(pathService, reason));
    }

    public Mono<ErrorRes> getErrorMono(String pathService, String reason) {
        if (reason.contains(Constants.BAD_REQUEST_HEADERS)) {
            return ErrorFactory.monoError(TechnicalExceptionEnum.TECHNICAL_HEADER_MISSING,
                    TechnicalExceptionEnum.TECHNICAL_HEADER_MISSING.getDescription(),
                    pathService);
        } else {
            return getAnotherErrors(pathService, reason);
        }
    }

    private Mono<ErrorRes> getAnotherErrors(String pathService, String reason) {
        if (reason.contains(Constants.BAD_PARAMETER)) {
            return ErrorFactory.monoError(TechnicalExceptionEnum.INVALID_PARAMS,
                    TechnicalExceptionEnum.INVALID_PARAMS.getDescription(),
                    pathService);
        } else if (reason.contains(Constants.NOT_FOUND)) {
            return ErrorFactory.monoError(TechnicalExceptionEnum.INVALID_REQUEST,
                    TechnicalExceptionEnum.INVALID_REQUEST.getDescription(),
                    pathService);
        }
        return ErrorFactory.monoError(TechnicalExceptionEnum.UNEXPECTED_EXCEPTION,
                HttpStatus.BAD_REQUEST.toString(), pathService);
    }

    public static Mono<String> getReason(ResponseStatusException responseException) {
        return just(responseException.getStatusCode().value())
                .map(String::valueOf)
                .map(status -> String.join(" ", status, responseException.getMessage()));
    }
}
