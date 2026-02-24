package co.com.nequi.log;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogBuilder {

    public static JsonLogMessage<Object> buildLogMessage(LogStructure logStructure) {
        return JsonLogMessage.<Object>builder()
                .id(UUID.randomUUID().toString())
                .messageId(logStructure.getMessageId())
                .action(logStructure.getAction())
                .timestamp(LogHelper.getLogTimestamp())
                .application(LogConstants.APPLICATION_NAME.getName())
                .service(LogConstants.SERVICE_NAME.getName())
                .component(logStructure.getComponent())
                .tags(logStructure.getTags())
                .data(getMessage(logStructure.getRequest(), logStructure.getResponse()))
                .build();
    }

    public static JsonLogMessage<Object> buildLogMessage(LogStructure logStructure, Throwable error) {
        return JsonLogMessage.<Object>builder()
                .id(UUID.randomUUID().toString())
                .messageId(logStructure.getMessageId())
                .action(logStructure.getAction())
                .timestamp(LogHelper.getLogTimestamp())
                .application(LogConstants.APPLICATION_NAME.getName())
                .service(LogConstants.SERVICE_NAME.getName())
                .component(logStructure.getComponent())
                .tags(logStructure.getTags())
                .data(getMessage(logStructure.getRequest(),
                        logStructure.getResponse(),
                        Objects.requireNonNullElse(error.getLocalizedMessage(),
                                LogConstants.EMPTY_STRING.getName())))
                .build();
    }

    private static Object getMessage(LogRequest logRequest, LogResponse logResponse) {
        return Map.of(
                LogConstants.REQUEST.getName(), getRequest(logRequest),
                LogConstants.RESPONSE.getName(), getResponse(logResponse)
        );
    }

    private static Object getMessage(LogRequest logRequest, LogResponse logResponse, String error) {
        return Map.of(
                LogConstants.REQUEST.getName(), getRequest(logRequest),
                LogConstants.RESPONSE.getName(), getResponse(logResponse),
                LogConstants.CAUSE.getName(), error
        );
    }

    private static Object getRequest(LogRequest logRequest) {
        return Map.of(
                LogConstants.TIMESTAMP.getName(), logRequest.getTimestamp(),
                LogConstants.HEADERS.getName(), logRequest.getHeaders(),
                LogConstants.BODY.getName(), logRequest.getBody()
        );
    }

    private static Object getResponse(LogResponse logResponse) {
        return Map.of(
                LogConstants.TIMESTAMP.getName(), logResponse.getTimestamp(),
                LogConstants.HEADERS.getName(), logResponse.getHeaders(),
                LogConstants.BODY.getName(), logResponse.getBody()
        );
    }

    @Data
    @Builder
    public static class LogStructure {
        private final String messageId;
        private final String action;
        private final String component;
        private final List<String> tags;
        private final LogRequest request;
        private final LogResponse response;
    }

    @Data
    @Builder
    public static class LogRequest {
        private final String timestamp;
        private final Map<String, String> headers;
        private final Object body;
    }

    @Data
    @Builder
    public static class LogResponse {
        private final String timestamp;
        private final Map<String, String> headers;
        private final Object body;
    }
}