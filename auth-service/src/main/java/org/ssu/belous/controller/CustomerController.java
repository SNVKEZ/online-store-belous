package org.ssu.belous.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssu.belous.dto.request.CustomerInfoRequestDto;
import org.ssu.belous.dto.response.ResponseDto;
import org.ssu.belous.security.JWTService;
import org.ssu.belous.service.CustomerService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final JWTService jwtService;

    @PutMapping("/add_customer_info")
    public ResponseEntity<ResponseDto> registration(@RequestHeader(value = "Authorization") String token, @RequestBody CustomerInfoRequestDto requestDto) {
        String username = jwtService.validTokenAndRetrieveSubject(token.substring("Bearer ".length())).get("username").asString();
        customerService.addCustomerInfo(username, requestDto);
        return ResponseEntity.ok()
                .body(ResponseDto.success().message("Персональная информация пользователя обновлена").showData(true).build());
    }
}
