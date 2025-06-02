package com.weatherfit.comment_service.auth.service;

import com.weatherfit.comment_service.auth.dto.EmailCodeRequestDTO;
import com.weatherfit.comment_service.auth.dto.EmailCodeVerifyDTO;
import com.weatherfit.comment_service.auth.dto.SignupTokenResponseDTO;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.dto.UserResponseDTO;
import com.weatherfit.comment_service.user.entity.User;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<UserResponseDTO> signup(Mono<UserRequestDTO> dtoMono);
    Mono<Void> sendCodeEmail(String to, String code);
    Mono<Void> requestEmailCode(Mono<EmailCodeRequestDTO> dtoMono);
    Mono<SignupTokenResponseDTO> verifyEmailCode(Mono<EmailCodeVerifyDTO> dtoMono);
}
