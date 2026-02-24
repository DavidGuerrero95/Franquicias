package co.com.nequi.api.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductStockUpdateRQ(@Valid Data data) {
    public record Data(@NotNull(message = "stock es obligatorio") @Min(value = 0, message = "stock debe ser >= 0") Integer stock) { }
}