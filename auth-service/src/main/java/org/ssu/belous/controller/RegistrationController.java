package org.ssu.belous.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssu.belous.dto.request.RegistrationRequestDto;
import org.ssu.belous.exception.UserAlreadyExistException;
import org.ssu.belous.service.RegistrationService;
import org.ssu.belous.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final UserService userService;
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(UserService userService, RegistrationService registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegistrationRequestDto requestDto) {
        try {
            registrationService.registration(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь создан");
        } catch (UserAlreadyExistException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
