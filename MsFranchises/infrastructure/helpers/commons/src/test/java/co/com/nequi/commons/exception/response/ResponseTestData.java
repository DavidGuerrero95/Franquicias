package co.com.nequi.commons.exception.response;

import java.util.List;

public final class ResponseTestData {
    private ResponseTestData() {
    }

    public static ErrorResponse sampleErrorResponse() {
        return ErrorResponse.builder()
                .code("E1")
                .message("msg")
                .domain("/dom")
                .reason("why")
                .type("T")
                .build();
    }

    public static ErrorsResp sampleErrorsResp() {
        return new ErrorsResp(List.of(sampleErrorResponse()));
    }
}