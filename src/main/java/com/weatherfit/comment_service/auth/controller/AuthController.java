package com.weatherfit.comment_service.auth.controller;

import com.weatherfit.comment_service.auth.dto.*;
import com.weatherfit.comment_service.auth.service.AuthService;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

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

    @PostMapping("/login")
    public Mono<ResponseEntity<JwtResponseDTO>> login(@RequestBody Mono<LoginRequestDTO> dtoMono) {
        return authService.login(dtoMono)
                .map(resp -> ResponseEntity.ok(resp));
    }

    @PatchMapping("/password")
    public Mono<ResponseEntity<JwtResponseDTO>> changePassword(@RequestParam String userId, @RequestParam String beforePwd, String afterPwd) {
        return authService.changePassword(userId, beforePwd, afterPwd)
                .map(resp -> ResponseEntity.ok(resp));
    }

}
