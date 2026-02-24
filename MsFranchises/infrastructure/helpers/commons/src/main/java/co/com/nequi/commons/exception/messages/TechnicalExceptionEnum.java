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
    TECHNICAL_SERVER_ERROR("MST005", "Internal server error", "Internal Server ErrorsResp"),

    SECRET_NOT_FOUND("MST010", "Secret not found", "The requested secret was not found"),
    SECRET_READ_ERROR("MST011", "Secret read error", "Error reading secret from Secrets Manager"),
    DB_CONNECTION_ERROR("MST020", "Database connection error", "Error creating database connection"),
    DB_OPERATION_ERROR("MST021", "Database operation error", "Error executing database operation");

    private final String code;
    private final String description;
    private final String message;
}