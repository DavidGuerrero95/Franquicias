package co.com.nequi.commons.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ErrorRes {

    private List<Data> errors;

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @Builder(toBuilder = true)
    public static class Data {
        private String type;
        private String reason;
        private String domain;
        private String code;
        private String message;
    }

}
