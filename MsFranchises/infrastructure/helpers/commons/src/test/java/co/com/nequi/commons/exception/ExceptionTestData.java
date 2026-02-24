package co.com.nequi.commons.exception;

import co.com.nequi.commons.error.ErrorRes;

import java.util.List;

public final class ExceptionTestData {
    private ExceptionTestData() {
    }

    public static ErrorRes errorResWithMessage(String code, String msg) {
        return ErrorRes.builder()
                .errors(List.of(ErrorRes.Data.builder()
                        .code(code)
                        .message(msg)
                        .domain("/test")
                        .reason("r")
                        .build()))
                .build();
    }

    public static ErrorRes nullErrors() {
        return new ErrorRes();
    }
}