package co.com.nequi.api.commons.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@UtilityClass
public class ResponseUtil {
    public static <T> Mono<ServerResponse> responseFailTechnical(T body){
        return buildResponse(INTERNAL_SERVER_ERROR, body);
    }
    public static <T> Mono<ServerResponse> responseFailBusiness(T body){
        return buildResponse(BAD_REQUEST, body);
    }
    public static <T> Mono<ServerResponse> buildResponse(HttpStatusCode status, T body){
        return ServerResponse
                .status(status)
                .contentType(APPLICATION_JSON)
                .bodyValue(body);
    }
}
