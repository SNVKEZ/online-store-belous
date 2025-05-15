package org.ssu.belous.controller;

import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.ssu.belous.dto.request.AuthorizeRequestDto;
import org.ssu.belous.security.JWTService;
import org.ssu.belous.service.UserService;
import org.ssu.belous.util.HeaderParsingUtils;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginPage(@RequestBody AuthorizeRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(requestDto.username(), requestDto.password());
        authenticationManager.authenticate(authToken);
        String token = jwtService.generateToken(requestDto.username(), userService.getRoleByUsername(requestDto.username()));
        return ResponseEntity.ok(Collections.singletonMap("Authorization", token));
    }

    @GetMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestHeader(value = "Authorization") String authorization) {
        Map<String, Claim> oldToken = jwtService.validTokenAndRetrieveSubject(HeaderParsingUtils.getTokenByAuthorizationHeader(authorization));
        String newToken = jwtService.generateToken(oldToken.get("username").asString(), oldToken.get("roles").asString());
        return ResponseEntity.ok(Collections.singletonMap("Authorization", newToken));
    }
}