package co.com.nequi.api.commons.exception;

import co.com.nequi.commons.exception.TechnicalException;
import co.com.nequi.commons.exception.messages.TechnicalExceptionEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
class ExceptionHandlerTest {

    @Mock
    ServerRequest serverRequest;

    @Mock
    ErrorAttributes errorAttributes;

    @Autowired
    ApplicationContext applicationContext;

    @Mock
    ServerCodecConfigurer serverCodecConfigurer;

    @Mock
    ServerRequest.Headers headers;

    ExceptionHandler exceptionHandler;

    HttpHeaders httpHeaders;

    @BeforeEach
    void init() {
        exceptionHandler = new ExceptionHandler(errorAttributes, applicationContext, serverCodecConfigurer);
        httpHeaders = new HttpHeaders();
        httpHeaders.set("aid-creator", "8944537565134848");
    }

    @Test
    void getResponseErrorTechnicalTest() {
        when(serverRequest.headers()).thenReturn(headers);
        when(headers.asHttpHeaders()).thenReturn(httpHeaders);
        when(errorAttributes.getError(any()))
                .thenReturn(new TechnicalException(new RuntimeException(),
                        TechnicalExceptionEnum.UNEXPECTED_EXCEPTION));
        Mono<ServerResponse> response = exceptionHandler.buildErrorResponse(serverRequest);
        response.subscribe();
        Assertions.assertNotNull(response);
    }

    @Test
    void getResponseErrorTest() {
        when(serverRequest.headers()).thenReturn(headers);
        when(headers.asHttpHeaders()).thenReturn(httpHeaders);
        when(errorAttributes.getError(any())).thenReturn(new NullPointerException());
        Mono<ServerResponse> response = exceptionHandler.buildErrorResponse(serverRequest);
        response.subscribe();
        Assertions.assertNotNull(response);
    }

}
