package co.com.nequi.commons.exception;

import co.com.nequi.commons.error.ErrorRes;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
public class ClientException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "Client error";

    private final transient ErrorRes error;
    private final HttpStatus status;

    public ClientException(ErrorRes error) {
        this(error, HttpStatus.BAD_REQUEST);
    }

    public ClientException(ErrorRes error, HttpStatus status) {
        super(Optional.ofNullable(error).map(ErrorRes::getErrors)
                .filter(list -> !list.isEmpty()).map(list -> list.getFirst().getMessage())
                .orElse(DEFAULT_ERROR_MESSAGE));
        this.error = error;
        this.status = status == null ? HttpStatus.BAD_REQUEST : status;
    }
}
