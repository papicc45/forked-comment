package com.weatherfit.comment_service.auth.service;

import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.entity.User;
import com.weatherfit.comment_service.user.mapper.UserMapper;
import com.weatherfit.comment_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public Mono<User> signup(Mono<UserRequestDTO> dtoMono) {
        return dtoMono
                .flatMap(dto ->
                        userRepository.existsByEmail(dto.getEmail())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new BusinessException(ErrorCode.AUTH_DUPLICATE_EMAIL));
                                    }
                                    User user = userMapper.DTOToUser(dto);
                                    return Mono.fromCallable(() ->
                                        passwordEncoder.encode(dto.getPassword()))
                                            .subscribeOn(Schedulers.boundedElastic())
                                            .map(encodedPassword -> {
                                                user.setPassword(encodedPassword);
                                                return user;
                                                    });
                                })
                        )
                .flatMap(userRepository::save);
    }
}
