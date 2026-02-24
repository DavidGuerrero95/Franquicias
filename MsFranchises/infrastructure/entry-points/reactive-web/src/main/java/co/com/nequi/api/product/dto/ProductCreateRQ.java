package co.com.nequi.api.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreateRQ(@Valid Data data) {
    public record Data(
            @NotBlank(message = "name no debe ser nulo o vacío") String name,
            @NotNull(message = "stock es obligatorio") @Min(value = 0, message = "stock debe ser >= 0") Integer stock
    ) { }
}