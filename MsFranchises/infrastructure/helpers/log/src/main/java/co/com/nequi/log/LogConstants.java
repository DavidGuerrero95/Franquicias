package co.com.nequi.log;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogConstants {

    APPLICATION_NAME("Nequi"),
    SERVICE_NAME("MsFranchises"),
    TIME_PATTERN("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
    CACHE_RESPONSE_BODY("cacheResponseBody"),
    CACHE_RESPONSE_INSTANT("CACHE_RESPONSE_INSTANT"),
    CACHE_REQUEST_INSTANT("CACHE_REQUEST_INSTANT"),
    CACHE_REQUEST_BODY("RequestBody"),
    MESSAGE_ID("message-id"),
    APP_VERSION("app-version"),
    BODY("body"),
    CAUSE("cause"),
    CHANNEL("channel"),
    EMPTY_STRING(""),
    EMPTY_JSON("{}"),
    HEADERS("headers"),
    REQUEST("request"),
    RESPONSE("response"),
    TIMESTAMP("timestamp"),
    TRACE("trace"),
    UTC("UTC"),
    URL("url"),
    EXCEPTION("exception"),
    START_NAME("Start"),
    END_NAME("End");

    private final String name;
}