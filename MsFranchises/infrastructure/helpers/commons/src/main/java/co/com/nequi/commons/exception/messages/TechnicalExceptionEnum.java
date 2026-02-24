package co.com.nequi.commons.exception.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalExceptionEnum {

    UNEXPECTED_EXCEPTION("MST001", "Unexpected Exception", "An unexpected error has occurred"),
    INVALID_REQUEST("MST002", "Request not found", "The request could not be found"),
    INVALID_PARAMS("MST003", "An error in params", "An error has occurred in the parameters provided"),
    TECHNICAL_HEADER_MISSING("MST004", "Missing parameters per header",
            "Required parameters are missing in the request header"),
    TECHNICAL_SERVER_ERROR("MST005", "Internal server error", "Internal Server ErrorsResp");

    private final String code;
    private final String description;
    private final String message;

}
