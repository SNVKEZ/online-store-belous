package org.ssu.belous.dto.request;

public record CustomerInfoRequestDto(String name, String second_name, String last_name,
                                     String city, String street, String number_home, String number_apartment) {
}
