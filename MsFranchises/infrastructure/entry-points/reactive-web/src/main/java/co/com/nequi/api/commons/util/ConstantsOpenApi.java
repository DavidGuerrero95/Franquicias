package co.com.nequi.api.commons.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstantsOpenApi {

    public static final String RESPONSE_ERROR_TECHNICAL = """
            {
            "reason": "Missing parameters per header",
            "domain": "/ms-franchises/api/v1/...",
            "code": "MST004",
            "message": "Missing parameters per header"
            }""";

    public static final String RESPONSE_ERROR_BUSINESS = """
            {
            "reason": "Request body error",
            "domain": "/ms-franchises/api/v1/...",
            "code": "MSB001",
            "message": "Request body error"
            }""";

}
