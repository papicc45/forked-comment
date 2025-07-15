package com.weatherfit.comment_service.user.service;

import com.weatherfit.comment_service.auth.service.AuthService;
import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.entity.User;
import com.weatherfit.comment_service.user.mapper.UserMapper;
import com.weatherfit.comment_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private static final String REDIS_KEY_FIND_ID = "FIND_ID_CODE:";


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final ReactiveRedisOperations<String, String> redisOperations;

    @Override
    public Mono<Boolean> duplicateCheck(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public Mono<Void> checkUserNameAndEmailMatch(String userName, String userEmail) {
        return userRepository.findByNicknameAndEmail(userName, userEmail)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(user -> {
                    String code = authService.generateRandomCode(15);
                    String key = REDIS_KEY_FIND_ID + userEmail;
                    return redisOperations
                            .opsForHash()
                            .putAll(key, Map.of(
                                    "code", code,
                                    "userId", user.getUserId()
                            ))
                            .then(redisOperations.expire(key, Duration.ofMinutes(5)))
                            .then(authService.sendCodeEmail(userEmail, code, 2));
                });
    }
}
