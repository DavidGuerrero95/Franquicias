package co.com.nequi.commons.exception.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionEnum {
    REQUEST_BODY("MSB001", "Request body error", "The request body is invalid or malformed.");
    private final String code;
    private final String description;
    private final String message;

}
