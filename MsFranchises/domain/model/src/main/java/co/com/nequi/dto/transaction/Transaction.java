package co.com.nequi.dto.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction implements Serializable, Cloneable {

    private Map<String, String> headers;
    private transient Object request;
    private transient Object response;
    private Integer statusCode;
    private boolean status;
    private String route;
    private String transactionCode;

    @Override
    public Transaction clone() {
        try {
            // copy mutable state here, so the clone can't change the internals of the original
            return (Transaction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
