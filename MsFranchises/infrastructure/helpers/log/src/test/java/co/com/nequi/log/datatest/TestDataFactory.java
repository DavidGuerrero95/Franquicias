package co.com.nequi.log.datatest;

import co.com.nequi.log.LogConstants;

import java.util.List;
import java.util.Map;

public final class TestDataFactory {
    public static final String PATH = "/ms-franchises/api/v1/test";
    public static final String MESSAGE_ID = "11111111-1111-1111-1111-111111111111";
    public static final String CHANNEL = "web";
    public static final String APP_VERSION = "1.0.0";

    private TestDataFactory() {
    }

    public static Map<String, Object> paramsForTechMessage() {
        return Map.of(
                LogConstants.URL.getName(), "/resource/action",
                LogConstants.HEADERS.getName(), Map.of(
                        LogConstants.REQUEST.getName(), Map.of("message-id", List.of(MESSAGE_ID)),
                        LogConstants.RESPONSE.getName(), Map.of("content-type", List.of("application/json"))
                ),
                LogConstants.START_NAME.getName(), 1000L,
                LogConstants.END_NAME.getName(), 2000L
        );
    }
}
