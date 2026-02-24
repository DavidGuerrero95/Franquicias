package co.com.nequi.api.products.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
@Setter
@Builder(toBuilder = true)
public class ProductDetailRQ implements Serializable {

    @NotNull
    @Valid
    private Data data;

    @AllArgsConstructor
    @NoArgsConstructor
    @lombok.Data
    @Builder(toBuilder = true)
    public static class Data implements Serializable {
        @NotEmpty(message = "No debe ser nulo o vacio.")
        @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Formato inválido: solo letras, números, _ y -")
        private String id;
    }
}