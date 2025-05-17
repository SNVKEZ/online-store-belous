package org.ssu.belous.dto;

import jakarta.validation.constraints.NotNull;

public record AddInventoryRequestDto(
        @NotNull
        String product,

        int quantity) {
}
