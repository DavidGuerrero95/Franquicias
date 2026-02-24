package co.com.nequi.api.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ProductRenameRQ(@Valid Data data) {
    public record Data(@NotBlank(message = "name no debe ser nulo o vacío") String name) { }
}