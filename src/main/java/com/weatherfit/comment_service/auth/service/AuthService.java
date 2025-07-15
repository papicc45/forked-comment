package com.weatherfit.comment_service.auth.service;

import com.weatherfit.comment_service.auth.dto.*;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.dto.UserResponseDTO;
import com.weatherfit.comment_service.user.entity.User;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface AuthService {
    Mono<UserResponseDTO> signup(Mono<UserRequestDTO> dtoMono);
    Mono<Void> sendCodeEmail(String to, String code, int type);
    Mono<Void> requestEmailCode(Mono<EmailCodeRequestDTO> dtoMono);
    Mono<SignupTokenResponseDTO> verifyEmailCode(Mono<EmailCodeVerifyDTO> dtoMono);
    Mono<JwtResponseDTO> login(Mono<LoginRequestDTO> dtoMono);
    Mono<JwtResponseDTO> changePassword(String userId, String beforePwd, String afterPwd);
    String generateRandomCode(int length);
    Mono<String> verifyFindUserIdCode(String userEmail, String code);
}
