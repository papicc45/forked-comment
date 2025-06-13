package com.weatherfit.comment_service.user.repository;

import com.weatherfit.comment_service.user.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<Boolean> existsByEmail(String email);
    Mono<User> findByUserId(String userId);
    Mono<Boolean> existsByUserId(String userId);
}
