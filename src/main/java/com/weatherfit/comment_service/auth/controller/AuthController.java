package com.weatherfit.comment_service.auth.controller;

import com.weatherfit.comment_service.auth.dto.EmailCodeRequestDTO;
import com.weatherfit.comment_service.auth.dto.EmailCodeVerifyDTO;
import com.weatherfit.comment_service.auth.dto.SignupTokenResponseDTO;
import com.weatherfit.comment_service.auth.service.AuthService;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public Mono<ResponseEntity<Void>> signup(@Valid @RequestBody Mono<UserRequestDTO> dtoMono) {
        return authService.signup(dtoMono)
                .map(user -> {
                    URI location = URI.create("/auth/signup" + user.getId());
                    return ResponseEntity.created(location).<Void>build();
                });
    }

    @PostMapping("/request-email-auth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> requestEmailAuth(@RequestBody Mono<EmailCodeRequestDTO> dtoMono) {
        return authService.requestEmailCode(dtoMono);
    }

    @PostMapping("/verify-code")
    public Mono<SignupTokenResponseDTO> verifyCode(@RequestBody Mono<EmailCodeVerifyDTO> dtoMono) {
        return authService.verifyEmailCode(dtoMono);
    }
}
