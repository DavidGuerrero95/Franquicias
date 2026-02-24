package co.com.nequi.commons.constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String MESSAGE_ID = "message-id";
    public static final String EMPTY = "";
    public static final String CHANNEL = "channel";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ACCEPT = "accept";
    public static final String SUCCESSFUL = "200";
    public static final String CONFLICT = "409";
    public static final String INTERNAL_SERVER_ERROR = "500";
    public static final String BAD_REQUEST_HEADERS = "ERROR EN HEADERS";
    public static final String BAD_PARAMETER = "ERROR EN PARAMETRO";
    public static final String NOT_FOUND = "NOT_FOUND";

    public static final String COLON_SEPARATOR = ": ";
    public static final String COMMA_SEPARATOR = ", ";
    public static final String VALIDATION_ERROR = "Error de Validación";

    public static final String PRODUCTS_PATH = "data/products.json";

}
