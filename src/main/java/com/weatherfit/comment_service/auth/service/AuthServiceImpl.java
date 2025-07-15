package com.weatherfit.comment_service.auth.service;

import com.weatherfit.comment_service.auth.dto.*;
import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.common.util.jwt.JwtTokenProvider;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.dto.UserResponseDTO;
import com.weatherfit.comment_service.user.entity.User;
import com.weatherfit.comment_service.user.mapper.UserMapper;
import com.weatherfit.comment_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ReactiveRedisOperations<String, String> redisOperations;
    private final JavaMailSender mailSender;
    private final JwtTokenProvider jwtTokenProvider;

    private static final Duration CODE_TTL = Duration.ofMinutes(5);
    private static final Duration TOKEN_TTL = Duration.ofHours(1);
    private static final String REDIS_KEY_VERIFIED = "email:verif:";
    private static final String REDIS_KEY_TOKEN = "x";
    private static final String REDIS_KEY_FIND_ID = "FIND_ID_CODE:";

    @Override
    public Mono<Void> sendCodeEmail(String to, String code, int type) {
        String subject = type == 1 ? "회원가입 이메일 인증번호 안내" : "아이디 찾기 인증번호 안내";
        String text = String.format("인증번호는 [%s] 입니다.\n" +
                "인증번호는 5분간 유효합니다.", code);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);

        return Mono.fromRunnable(() -> mailSender.send(msg))
                .subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<Void> requestEmailCode(Mono<EmailCodeRequestDTO> dtoMono) {
        return dtoMono.flatMap(dto -> {
            String email = dto.getEmail();
            String code = generateRandomCode(15);
            String key = REDIS_KEY_VERIFIED + email;

            /** Redis에 (email→code) 저장하고 TTL 설정*/
            return redisOperations.opsForValue()
                    .set(key, code, CODE_TTL)
                    /** 이메일로 코드 발송*/
                    .then(sendCodeEmail(email, code, 1));
        });
    }

    @Override
    public Mono<SignupTokenResponseDTO> verifyEmailCode(Mono<EmailCodeVerifyDTO> dtoMono) {
        return dtoMono.flatMap(dto -> {
            String verifiedKey = REDIS_KEY_VERIFIED + dto.getEmail();
            String tokenKey = REDIS_KEY_TOKEN + dto.getCode();

            return redisOperations.opsForValue().get(verifiedKey)
                    .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.EXPIRED_TOKEN)))
                    .flatMap(storedCode -> {
                        if(!storedCode.equals(String.valueOf(dto.getCode()))) {
                            return Mono.error(new BusinessException(ErrorCode.AUTH_NUMBER_NOT_MATCH));
                        }
                        return redisOperations.opsForValue().delete(verifiedKey)
                                .then(redisOperations.opsForValue().set(tokenKey, "true", TOKEN_TTL)) /** 회원가입 시 이메일 인증 거쳤는지 확인 용도*/
                                .thenReturn(new SignupTokenResponseDTO(tokenKey));
                    });
        });
    }

    /** 이메일 인증번호 생성*/
    @Override
    public String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(random.nextInt(9)+1);
        for(int i = 0; i < length-1; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    @Override
    @Transactional
    public Mono<UserResponseDTO> signup(Mono<UserRequestDTO> dtoMono) {
        return dtoMono
                .flatMap(dto -> {
                    return redisOperations.opsForValue().get(dto.getToken())
                            .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.EXPIRED_TOKEN)))
                            .flatMap(flag -> {
                                if(!"true".equals(flag)) {
                                    return Mono.error(new BusinessException(ErrorCode.AUTH_NUMBER_NOT_MATCH));
                                }

                                User user = userMapper.DTOToUser(dto);
                                return Mono.fromCallable(() ->
                                                passwordEncoder.encode(dto.getPassword()))
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .map(encodedPassword -> {
                                            user.setPassword(encodedPassword);
                                            return user;
                                        });
                            });

                })
                .flatMap(userRepository::save)
                .flatMap(saveUser -> {
                    String verifiedKey = REDIS_KEY_TOKEN + saveUser.getEmail();
                    return redisOperations.opsForValue()
                            .delete(verifiedKey)
                            .thenReturn(saveUser);
                })
                .map(userMapper::userToDTO);
    }

    @Override
    public Mono<JwtResponseDTO> login(Mono<LoginRequestDTO> dtoMono) {
        return dtoMono
                .flatMap(dto ->
                        userRepository.findByUserId(dto.getId())
                                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND)))
                                .flatMap(user -> {
                                    if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                                        return Mono.error(new BusinessException(ErrorCode.PASSWORD_NOT_MATCH));
                                    }
                                    LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMinutes(6);

                                    String token = jwtTokenProvider.createToken(user.getUserId());
                                    return user.getPasswordChangedAt().isBefore(sixMonthsAgo) ? Mono.just(new JwtResponseDTO(token, true)) : Mono.just(new JwtResponseDTO(token, false));
                                })
                        );
    }

    @Override
    public Mono<JwtResponseDTO> changePassword(String userId, String beforePwd, String afterPwd) {
        return userRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(user -> {
                    if(!passwordEncoder.matches(beforePwd, user.getPassword())) {
                        return Mono.error(new BusinessException(ErrorCode.PASSWORD_NOT_MATCH));
                    }

                    return Mono.fromCallable(() ->
                                passwordEncoder.encode(afterPwd))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(encoded -> {
                                user.setPassword(encoded);
                                user.setPasswordChangedAt(LocalDateTime.now());

                                return userRepository.save(user);
                            });
                })
                .map(updatedUser -> {
                    String newToken = jwtTokenProvider.createToken(updatedUser.getUserId());

                    return new JwtResponseDTO(newToken, false);
                });
    }

    @Override
    public Mono<String> verifyFindUserIdCode(String userEmail, String code) {
        String key = REDIS_KEY_FIND_ID + userEmail;

        return redisOperations
                .<String, String>opsForHash()
                .entries(key)
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .flatMap(map -> {
                    String storedCode = map.get("code");
                    if(!code.equals(storedCode)) {
                        return Mono.error(new BusinessException(ErrorCode.AUTH_NUMBER_NOT_MATCH));
                    }

                    return Mono.just(map.get("userId"));
                });
    }
}
