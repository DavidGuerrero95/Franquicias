package co.com.nequi.api.branch.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record BranchRenameRQ(@Valid Data data) {
    public record Data(@NotBlank(message = "name no debe ser nulo o vacío") String name) { }
}