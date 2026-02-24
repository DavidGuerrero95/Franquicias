package co.com.nequi.log;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class TechMessage {

    public JsonLogMessage<Object> buildTechMessageError(@NonNull Throwable error,
                                                        ServerRequest request,
                                                        String clazzName) {
        return JsonLogMessage.builder()
                .id(UUID.randomUUID().toString())
                .messageId(ServerWebExchangeHelper.getFirstHeader(request, LogConstants.MESSAGE_ID))
                .action(request.path())
                .timestamp(ServerWebExchangeHelper.getTimeStampFormatted(System.currentTimeMillis()))
                .application(LogConstants.SERVICE_NAME.getName())
                .service(LogConstants.SERVICE_NAME.getName())
                .component(clazzName)
                .tags(ServerWebExchangeHelper.getTagList(
                        ServerWebExchangeHelper.getFirstHeader(request, LogConstants.CHANNEL),
                        ServerWebExchangeHelper.getFirstHeader(request, LogConstants.APP_VERSION)))
                .data(TechMessageHelper.getErrorObjectMessage(error))
                .build();
    }

    public JsonLogMessage<Object> buildTechMessage(ServerWebExchange webExchange, String clazzName) {
        return JsonLogMessage.builder()
                .id(UUID.randomUUID().toString())
                .messageId(ServerWebExchangeHelper.getFirstHeader(webExchange, LogConstants.MESSAGE_ID))
                .action(webExchange.getRequest().getPath().value())
                .timestamp(ServerWebExchangeHelper.getTimeStampFormatted(System.currentTimeMillis()))
                .application(LogConstants.SERVICE_NAME.getName())
                .service(LogConstants.SERVICE_NAME.getName())
                .component(clazzName)
                .tags(ServerWebExchangeHelper.getTagList(
                        ServerWebExchangeHelper.getFirstHeader(webExchange, LogConstants.CHANNEL),
                        ServerWebExchangeHelper.getFirstHeader(webExchange, LogConstants.APP_VERSION)))
                .data(ServerWebExchangeHelper.getMessage(webExchange))
                .build();
    }

    public JsonLogMessage<Object> buildTechMessage(Object request, Object response,
                                                   Map<String, Serializable> params, String clazzName) {
        var headersRequest = Optional.ofNullable((HttpHeaders) params.get(
                LogConstants.HEADERS.getName().concat(LogConstants.REQUEST.getName()))).orElse(new HttpHeaders());
        var headersResponse = Optional.ofNullable((HttpHeaders) params.get(
                LogConstants.HEADERS.getName().concat(LogConstants.RESPONSE.getName()))).orElse(new HttpHeaders());
        var startTime = Optional.ofNullable(params.get(LogConstants.START_NAME.getName()))
                .orElse(System.currentTimeMillis());
        var endTime = Optional.ofNullable(params.get(LogConstants.END_NAME.getName()))
                .orElse(System.currentTimeMillis());
        var url = Optional.ofNullable(params.get(LogConstants.URL.getName()))
                .map(Object::toString).orElse("unknown");
        var safeRequest = Optional.ofNullable(request).orElse(Map.of());
        var safeResponse = Optional.ofNullable(response).orElse(Map.of());
        var message = Map.of(
                LogConstants.REQUEST.getName(), safeRequest,
                LogConstants.RESPONSE.getName(), safeResponse,
                LogConstants.HEADERS.getName(), Map.of(
                        LogConstants.REQUEST.getName(), TechMessageHelper.getHeaders(headersRequest),
                        LogConstants.RESPONSE.getName(), TechMessageHelper.getHeaders(headersResponse)
                ),
                LogConstants.START_NAME.getName(), startTime,
                LogConstants.END_NAME.getName(), endTime
        );

        return JsonLogMessage.builder()
                .id(UUID.randomUUID().toString())
                .messageId(TechMessageHelper.getFirstHeader(headersRequest, LogConstants.MESSAGE_ID))
                .action(url)
                .timestamp(ServerWebExchangeHelper.getTimeStampFormatted(System.currentTimeMillis()))
                .application(LogConstants.SERVICE_NAME.getName())
                .service(LogConstants.SERVICE_NAME.getName())
                .component(clazzName)
                .data(TechMessageHelper.getInfoMessageRequestResponse(message))
                .build();
    }

    public JsonLogMessage<Object> buildTechMessageError(@NonNull Throwable error,
                                                        String name,
                                                        String clazzName) {
        return JsonLogMessage.builder()
                .id(UUID.randomUUID().toString())
                .messageId(null)
                .action(name)
                .timestamp(ServerWebExchangeHelper.getTimeStampFormatted(System.currentTimeMillis()))
                .application(LogConstants.SERVICE_NAME.getName())
                .service(LogConstants.SERVICE_NAME.getName())
                .component(clazzName)
                .data(TechMessageHelper.getErrorObjectMessage(error))
                .build();
    }
}
