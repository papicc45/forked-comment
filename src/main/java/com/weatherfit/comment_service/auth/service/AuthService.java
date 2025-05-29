package com.weatherfit.comment_service.auth.service;

import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.entity.User;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<User> signup(Mono<UserRequestDTO> dtoMono);
}
