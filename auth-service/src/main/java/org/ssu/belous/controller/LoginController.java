package org.ssu.belous.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.ssu.belous.dto.request.AuthorizeRequestDto;
import org.ssu.belous.model.User;
import org.ssu.belous.security.JWTService;
import org.ssu.belous.service.UserService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    public LoginController(UserService userService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginPage(@RequestBody AuthorizeRequestDto requestDto) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(requestDto.username(),requestDto.password());
        try {
            authenticationManager.authenticate(authToken);
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Неверный логин или пароль"));
        }

        String token = jwtService.generateToken(requestDto.username(), userService.getRoleByUsername(requestDto.username()));
        return ResponseEntity.ok(Collections.singletonMap("jwt-token", token));
    }
}