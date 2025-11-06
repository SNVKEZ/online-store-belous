package org.ssu.belous.dto;


import jakarta.validation.constraints.NotNull;

public record AddInventoryCost(
        @NotNull
        String product,
        @NotNull
        double cost
) {
}