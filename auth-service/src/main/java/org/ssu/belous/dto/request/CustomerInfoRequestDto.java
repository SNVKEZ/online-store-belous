package org.ssu.belous.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CustomerInfoRequestDto(
        @NotBlank(message = "Имя не может быть пустым")
        String name,

        @NotBlank(message = "Фамилия не может быть пустым")
        String second_name,

        @NotBlank(message = "Отчество не может быть пустым")
        String last_name,

        @NotBlank(message = "Город не может быть пустым")
        String city,

        @NotBlank(message = "Улица не может быть пустым")
        String street,

        @NotBlank(message = "Номер дома не может быть пустым")
        String number_home,

        String number_apartment) {
}
