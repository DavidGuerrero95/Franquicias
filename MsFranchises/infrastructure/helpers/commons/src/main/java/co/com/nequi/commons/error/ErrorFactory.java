package co.com.nequi.commons.error;

import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.TechnicalException;
import co.com.nequi.commons.exception.messages.BusinessExceptionEnum;
import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.util.List;

@UtilityClass
public class ErrorFactory {

    public ErrorRes buildError(TechnicalExceptionEnum technicalExceptionEnum, String reason, String domain) {
        return ErrorRes.builder()
                .errors(List.of(ErrorRes.Data.builder()
                        .reason(reason)
                        .domain(domain)
                        .code(technicalExceptionEnum.getCode())
                        .message(technicalExceptionEnum.getDescription())
                        .build()))
                .build();
    }

    public ErrorRes buildError(BusinessExceptionEnum businessException, String reason, String domain) {
        return ErrorRes.builder()
                .errors(List.of(ErrorRes.Data.builder()
                        .reason(reason)
                        .domain(domain)
                        .code(businessException.getCode())
                        .message(businessException.getMessage())
                        .build()))
                .build();
    }

    public Mono<ErrorRes> monoError(TechnicalExceptionEnum technicalException, String reason, String domain) {
        return Mono.just(buildError(technicalException, reason, domain));
    }

    public Mono<ErrorRes> monoError(BusinessExceptionEnum businessException, String reason, String domain) {
        return Mono.just(buildError(businessException, reason, domain));
    }

    public Mono<ErrorRes> fromTechnical(TechnicalException technicalException, String domain) {
        return monoError(technicalException.getException(), technicalException.getMessage(), domain);
    }

    public Mono<ErrorRes> fromBusiness(BusinessException businessException, String domain) {
        return Mono.just(businessException.getError())
                .filter(ex -> ex.getErrors() != null)
                .switchIfEmpty(Mono.defer(() ->
                        monoError(businessException.getBusinessExceptionEnum(),
                                businessException.getMessage(), domain)));
    }

    public Mono<ErrorRes> fromDefaultTechnical(String reason, String domain) {
        return monoError(TechnicalExceptionEnum.TECHNICAL_SERVER_ERROR, reason, domain);
    }

}
