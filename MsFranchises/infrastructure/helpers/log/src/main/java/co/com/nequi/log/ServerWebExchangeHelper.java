package co.com.nequi.log;


import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static co.com.nequi.log.LogConstants.APP_VERSION;
import static co.com.nequi.log.LogConstants.BODY;
import static co.com.nequi.log.LogConstants.CACHE_REQUEST_BODY;
import static co.com.nequi.log.LogConstants.CACHE_RESPONSE_BODY;
import static co.com.nequi.log.LogConstants.EMPTY_JSON;
import static co.com.nequi.log.LogConstants.EMPTY_STRING;
import static co.com.nequi.log.LogConstants.HEADERS;
import static co.com.nequi.log.LogConstants.REQUEST;
import static co.com.nequi.log.LogConstants.RESPONSE;
import static co.com.nequi.log.LogConstants.TIMESTAMP;
import static co.com.nequi.log.LogConstants.TIME_PATTERN;

@UtilityClass
public class ServerWebExchangeHelper {

    public Map<String, Object> getMessage(ServerWebExchange exchange) {
        return Map.of(REQUEST.getName(), getRequest(exchange),
                RESPONSE.getName(), getResponse(exchange));
    }

    public Map<String, Object> getMessage(ServerWebExchange exchange, Object errorResponse) {
        return Map.of(REQUEST.getName(), getRequest(exchange),
                RESPONSE.getName(), getResponse(exchange, errorResponse));
    }

    private static Map<String, Object> getRequest(ServerWebExchange exchange) {
        String cachedBody = exchange.getAttribute(CACHE_REQUEST_BODY.getName());
        Object parsedBody = cachedBody != null ? JsonSerializerHelper.getBodyAsObject(cachedBody) : "{}";

        return Map.of(
                TIMESTAMP.getName(), getTimeStampFormatted(System.currentTimeMillis()),
                HEADERS.getName(), getRequestHeader(exchange),
                BODY.getName(), parsedBody
        );
    }

    private static Map<String, Object> getResponse(ServerWebExchange exchange, Object errorResponse) {
        return Map.of(
                TIMESTAMP.getName(), getTimeStampFormatted(System.currentTimeMillis()),
                HEADERS.getName(), getResponseHeader(exchange),
                BODY.getName(), errorResponse
        );
    }

    private static Map<String, Object> getResponse(ServerWebExchange exchange) {
        return Map.of(
                TIMESTAMP.getName(), getTimeStampFormatted(System.currentTimeMillis()),
                HEADERS.getName(), getResponseHeader(exchange),
                BODY.getName(), JsonSerializerHelper.getBodyAsObject(getAttributeFromExchange(exchange, CACHE_RESPONSE_BODY))
        );
    }

    public String getFirstHeader(ServerRequest request, LogConstants name) {
        return Optional.ofNullable(request)
                .map(ServerRequest::headers)
                .map(headers -> headers.firstHeader(name.getName()))
                .orElse(EMPTY_STRING.getName());
    }

    public String getFirstHeader(ServerWebExchange exchange, LogConstants name) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getRequest)
                .map(HttpMessage::getHeaders)
                .map(headers -> headers.getFirst(name.getName()))
                .orElse(EMPTY_STRING.getName());
    }

    private static Map<String, String> getRequestHeader(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getRequest)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Map.of(EMPTY_STRING.getName(), EMPTY_STRING.getName()));
    }

    private static Map<String, String> getResponseHeader(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getResponse)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Map.of(EMPTY_STRING.getName(), EMPTY_STRING.getName()));
    }

    private static String formatAppVersion(String appVersion) {
        return String.join(" ", APP_VERSION.getName(), appVersion);
    }

    public String getTimeStampFormatted(Long currentTimeMillis) {
        var dateFormat = new SimpleDateFormat(TIME_PATTERN.getName());
        return dateFormat.format(Date.from(Instant.ofEpochMilli(currentTimeMillis)));
    }

    public List<String> getTagList(String channel, String appVersion) {
        return List.of(channel, formatAppVersion(appVersion));
    }

    private static Object getAttributeFromExchange(ServerWebExchange exchange, LogConstants name) {
        return Optional.ofNullable(exchange)
                .map(ex -> exchange.getAttribute(name.getName()))
                .orElse(EMPTY_JSON.getName());
    }
}