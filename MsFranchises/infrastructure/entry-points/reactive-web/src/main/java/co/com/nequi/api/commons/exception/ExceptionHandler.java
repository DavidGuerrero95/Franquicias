package co.com.nequi.api.commons.exception;

import co.com.nequi.api.commons.util.ExceptionUtil;
import co.com.nequi.api.commons.util.ResponseUtil;
import co.com.nequi.commons.error.ErrorFactory;
import co.com.nequi.commons.error.ErrorRes;
import co.com.nequi.commons.exception.BusinessException;
import co.com.nequi.commons.exception.ClientException;
import co.com.nequi.commons.exception.ServerException;
import co.com.nequi.commons.exception.TechnicalException;
import co.com.nequi.log.LogWriter;
import co.com.nequi.log.TechMessage;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.stream.Collectors;

@Order(-2)
@Component
public class ExceptionHandler extends AbstractErrorWebExceptionHandler {

    public ExceptionHandler(ErrorAttributes errorAttributes, ApplicationContext applicationContext,
                            ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::buildErrorResponse);
    }

    public Mono<ServerResponse> buildErrorResponse(ServerRequest request) {
        return accessError(request)
                .flatMap(Mono::error)
                .onErrorResume(TechnicalException.class, responseTechnicalError(request))
                .onErrorResume(BusinessException.class, responseBusinessError(request))
                .onErrorResume(ClientException.class, responseClientError(request))
                .onErrorResume(ServerException.class, responseServerError(request))
                .onErrorResume(ResponseStatusException.class, responseStatusError(request))
                .onErrorResume(responseDefaultError(request))
                .cast(ServerResponse.class);
    }

    private Mono<Throwable> accessError(ServerRequest request) {
        return Mono.just(request)
                .map(this::getError)
                .doOnNext(error -> logError(request, error))
                .thenReturn(getError(request));
    }

    private Function<TechnicalException, Mono<ServerResponse>> responseTechnicalError(ServerRequest request) {
        return e -> ErrorFactory.fromTechnical(e, request.uri().toString())
                .flatMap(ResponseUtil::responseFailTechnical);
    }

    private Function<BusinessException, Mono<ServerResponse>> responseBusinessError(ServerRequest request) {
        return e -> ErrorFactory.fromBusiness(e, request.path())
                .flatMap(ResponseUtil::responseFailBusiness);
    }

    private Function<ClientException, Mono<ServerResponse>> responseClientError(ServerRequest request) {
        return e -> {
            var body = overrideDomainIfNeeded(e.getError(), request.path());
            return ResponseUtil.buildResponse(e.getStatus(), body);
        };
    }

    private Function<ServerException, Mono<ServerResponse>> responseServerError(ServerRequest request) {
        return e -> {
            var body = overrideDomainIfNeeded(e.getError(), request.path());
            return ResponseUtil.buildResponse(e.getStatus(), body);
        };
    }

    private Function<ResponseStatusException, Mono<ServerResponse>> responseStatusError(ServerRequest request) {
        return responseStatusException -> ExceptionUtil.fromResponseStatus(request.uri().toString())
                .apply(responseStatusException)
                .flatMap(error -> ResponseUtil.buildResponse(responseStatusException.getStatusCode(), error));
    }

    private Function<Throwable, Mono<ServerResponse>> responseDefaultError(ServerRequest serverRequest) {
        return exception -> ErrorFactory.fromDefaultTechnical(exception.getMessage()
                        , serverRequest.uri().toString())
                .flatMap(ResponseUtil::responseFailTechnical);
    }

    private void logError(ServerRequest request, Throwable error) {
        LogWriter.error(
                TechMessage.buildTechMessageError(
                        error,
                        request,
                        ExceptionHandler.class.getName()
                )
        );
    }

    private ErrorRes overrideDomainIfNeeded(ErrorRes original, String domain) {
        if (original == null || original.getErrors() == null) return original;
        var fixed = original.getErrors().stream().map(e ->
                ErrorRes.Data.builder()
                        .type(e.getType())
                        .reason(e.getReason())
                        .domain(domain)
                        .code(e.getCode())
                        .message(e.getMessage())
                        .build()
        ).collect(Collectors.toList());
        return ErrorRes.builder().errors(fixed).build();
    }
}