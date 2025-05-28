package com.weatherfit.comment_service.user.repository;

import com.weatherfit.comment_service.user.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
