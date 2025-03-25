package org.ssu.belous.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.ssu.belous.dto.request.CustomerInfoRequestDto;
import org.ssu.belous.dto.request.RegistrationRequestDto;
import org.ssu.belous.exception.UserAlreadyExistException;
import org.ssu.belous.security.JWTService;
import org.ssu.belous.security.helpers.JWTVerifier;
import org.ssu.belous.service.CustomerService;

@RestController
@RequestMapping("/api/auth")
public class CustomerController {
    private final CustomerService customerService;
    private final JWTService jwtService;

    @Autowired
    public CustomerController(CustomerService customerService, JWTService jwtService) {
        this.customerService = customerService;
        this.jwtService = jwtService;
    }

    @PutMapping("/add_customer_info")
    public ResponseEntity<String> registration(@RequestHeader(value = "Authorization") String token, @RequestBody CustomerInfoRequestDto requestDto) {
        try {
            String username = jwtService.validTokenAndRetrieveSubject(token.substring("Bearer ".length())).get("username").asString();
            customerService.addCustomerInfo(username, requestDto);
            return ResponseEntity.status(HttpStatus.OK).body("Персональная информация пользователя обновлена");
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
