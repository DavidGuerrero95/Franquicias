package co.com.nequi.api.franchise.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record FranchiseCreateRQ(@Valid Data data) {
    public record Data(@NotBlank(message = "name no debe ser nulo o vacío") String name) { }
}