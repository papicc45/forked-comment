package com.weatherfit.comment_service.auth.service;

import com.weatherfit.comment_service.auth.dto.EmailCodeRequestDTO;
import com.weatherfit.comment_service.auth.dto.EmailCodeVerifyDTO;
import com.weatherfit.comment_service.auth.dto.SignupTokenResponseDTO;
import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.dto.UserResponseDTO;
import com.weatherfit.comment_service.user.entity.User;
import com.weatherfit.comment_service.user.mapper.UserMapper;
import com.weatherfit.comment_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ReactiveRedisOperations<String, String> redisOperations;
    private final JavaMailSender mailSender;

    private static final Duration CODE_TTL = Duration.ofMinutes(5);
    private static final Duration TOKEN_TTL = Duration.ofHours(1);
    private static final String REDIS_KEY_VERIFIED = "email:verif:";
    private static final String REDIS_KEY_TOKEN = "email:sign:";

    @Override
    public Mono<Void> sendCodeEmail(String to, String code) {
        String subject = "회원가입 이메일 인증번호 안내";
        String text = String.format("안녕하세요.\n회원가입을 위한 인증번호는 [%s] 입니다.\n" +
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

            // 1) Redis에 (email→code) 저장하고 TTL 설정
            return redisOperations.opsForValue()
                    .set(key, code, CODE_TTL)
                    // 2) 이메일로 코드 발송하기
                    .then(sendCodeEmail(email, code));
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
                                .then(redisOperations.opsForValue().set(tokenKey, "true", TOKEN_TTL))
                                .thenReturn(new SignupTokenResponseDTO(tokenKey));
                    });
        });
    }

    /** 이메일 인증번호 생성*/
    private String generateRandomCode(int length) {
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

}
