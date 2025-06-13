package com.weatherfit.comment_service.user.service;

import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Boolean> duplicateCheck(String userId);
}
