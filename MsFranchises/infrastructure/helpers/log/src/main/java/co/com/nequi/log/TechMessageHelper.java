package co.com.nequi.log;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.com.nequi.log.LogConstants.BODY;
import static co.com.nequi.log.LogConstants.CAUSE;
import static co.com.nequi.log.LogConstants.EMPTY_STRING;
import static co.com.nequi.log.LogConstants.EXCEPTION;
import static co.com.nequi.log.LogConstants.HEADERS;
import static co.com.nequi.log.LogConstants.REQUEST;
import static co.com.nequi.log.LogConstants.RESPONSE;
import static co.com.nequi.log.LogConstants.TIMESTAMP;
import static co.com.nequi.log.LogConstants.TRACE;

@UtilityClass
public class TechMessageHelper {

    private static final String DELIMITER = ",";

    private static final String START_NAME = "Start";
    private static final String END_NAME = "End";

    public Map<String, Object> getErrorObjectMessage(Throwable error) {
        final Map<String, Object> errorMap = new HashMap<>();
        Optional.ofNullable(error)
                .map(Throwable::getStackTrace).ifPresent(trace -> errorMap.put(TRACE.getName(), trace));
        Optional.ofNullable(error)
                .map(Throwable::getMessage).ifPresent(message -> errorMap.put(CAUSE.getName(), message));
        Optional.ofNullable(error)
                .map(Throwable::toString).ifPresent(stringError -> errorMap.put(EXCEPTION.getName(), stringError));

        return errorMap;
    }

    public String getHeader(Map<String, List<String>> headers, String key) {
        return String.join(DELIMITER, headers.getOrDefault(key, List.of(EMPTY_STRING.getName())));
    }

    public Map<String, String> getHeaders(Map<String, List<String>> headers) {
        if (headers == null) {
            return Map.of();
        }
        return headers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> String.join(DELIMITER, e.getValue())));
    }

    public String getFirstHeader(HttpHeaders headers, LogConstants name) {
        return Optional.ofNullable(headers)
                .map(httpHeaders -> headers.getFirst(name.getName()))
                .orElse(EMPTY_STRING.getName());
    }

    public Map<String, Object> getInfoMessageRequestResponse(Map<String, Object> parameters) {
        Map<String, Object> messageJSON = new HashMap<>();
        messageJSON.put(REQUEST.getName(), Map.of(
                HEADERS.getName(), ((Map<?, ?>) parameters.get(HEADERS.getName())).get(REQUEST.getName()),
                BODY.getName(), JsonSerializerHelper.getBodyAsObject(parameters.get(REQUEST.getName())),
                TIMESTAMP.getName(), parameters.get(START_NAME).toString()
        ));

        messageJSON.put(RESPONSE.getName(), Map.of(
                HEADERS.getName(), ((Map<?, ?>) parameters.get(HEADERS.getName())).get(RESPONSE.getName()),
                BODY.getName(), JsonSerializerHelper.getBodyAsObject(parameters.get(RESPONSE.getName())),
                TIMESTAMP.getName(), parameters.get(END_NAME).toString()
        ));

        return messageJSON;
    }
}
