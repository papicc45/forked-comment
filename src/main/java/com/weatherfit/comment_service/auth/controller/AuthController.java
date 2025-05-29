package com.weatherfit.comment_service.auth.controller;

import com.weatherfit.comment_service.auth.service.AuthService;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
