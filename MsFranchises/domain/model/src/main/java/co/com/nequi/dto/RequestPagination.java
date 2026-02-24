package co.com.nequi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestPagination {

    @NotNull(message = "pageSize es obligatorio")
    @Min(value = 1, message = "pageSize debe ser >= 1")
    @Max(value = 30, message = "pageSize debe ser <= 30")
    @JsonProperty(value = "pageSize", required = true)
    private Integer pageSize;

    @NotNull(message = "pageNumber es obligatorio")
    @Positive(message = "pageNumber debe ser > 0")
    @Max(value = Integer.MAX_VALUE - 1L, message = "pageNumber debe ser menor a 2147483647")
    @JsonProperty(value = "pageNumber", required = true)
    private Integer pageNumber;
}
