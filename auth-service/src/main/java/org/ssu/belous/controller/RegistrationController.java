package org.ssu.belous.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssu.belous.dto.request.RegistrationRequestDto;
import org.ssu.belous.dto.response.ResponseDto;
import org.ssu.belous.service.RegistrationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public ResponseEntity<ResponseDto> registration(@Valid @RequestBody RegistrationRequestDto requestDto) {
        registrationService.registration(requestDto);
        return ResponseEntity.ok()
                .body(ResponseDto.success().message("Пользователь успешно создан").showData(true).build());
    }
}
