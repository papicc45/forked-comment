package com.weatherfit.comment_service.auth;

import com.weatherfit.comment_service.auth.service.AuthServiceImpl;
import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.common.util.jwt.JwtTokenProvider;
import com.weatherfit.comment_service.user.dto.UserRequestDTO;
import com.weatherfit.comment_service.user.dto.UserResponseDTO;
import com.weatherfit.comment_service.user.entity.User;
import com.weatherfit.comment_service.user.mapper.UserMapper;
import com.weatherfit.comment_service.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private UserMapper userMapper;
    @Mock private ReactiveRedisOperations<String,String> reactiveRedisOperations;
    @Mock private ReactiveValueOperations<String,String> valueOperations;
    @Mock private ReactiveHashOperations<String,String,String> hashOperations;
    @InjectMocks private AuthServiceImpl authService;

    private static final String REDIS_KEY_FIND_ID = "FIND_ID_CODE:";

    @BeforeEach
    void setUp() {
        lenient().when(reactiveRedisOperations.opsForValue())
                .thenReturn(valueOperations);
        lenient().when(reactiveRedisOperations.<String, String>opsForHash())
                .thenReturn(hashOperations);
    }
    @Test
    void signup_success() {
        UserRequestDTO dto = new UserRequestDTO(
                "alice@example.com", "password", "dongjun",
                "nick", "emailaa", "01022222222", "token456"
        );

        User user = new User();
        user.setUserId(dto.getUserId());
        user.setEmail(dto.getEmail());
        user.setPassword("raw");
        LocalDateTime now = LocalDateTime.now();
        ReflectionTestUtils.setField(user, "createdDate", now);
        ReflectionTestUtils.setField(user, "modifiedDate", now);

        when(valueOperations.get("token456"))
                .thenReturn(Mono.just("true"));
        when(userMapper.DTOToUser(dto))
                .thenReturn(user);
        when(passwordEncoder.encode("password"))
                .thenReturn("encodedPwd");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(captor.capture()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(valueOperations.delete(anyString()))
                .thenReturn(Mono.just(true));

        UserResponseDTO responseDTO = new UserResponseDTO(
                user.getId(),
                user.getUserId(),
                user.getName(),
                user.getNickname(),
                user.getCreatedDate().toString(),
                user.getModifiedDate().toString()
        );
        when(userMapper.userToDTO(user))
                .thenReturn(responseDTO);

        StepVerifier.create(authService.signup(Mono.just(dto)))
                .assertNext(resp -> {
                    User saved = captor.getValue();
                    assert saved.getPassword().equals("encodedPwd");
                    assert resp.getUserId().equals(responseDTO.getUserId());
                })
                .verifyComplete();
    }

    @Test
    void signup_authNumberNotMatch() {
        UserRequestDTO dto = new UserRequestDTO(
                "alice@example.com", "password", "dongjun",
                "nick", "emailaa", "01022222222", "token456"
        );
        when(valueOperations.get("token456"))
                .thenReturn(Mono.just("false"));

        StepVerifier.create(authService.signup(Mono.just(dto)))
                .expectErrorMatches(ex ->
                        ((BusinessException) ex).getErrorCode() == ErrorCode.AUTH_NUMBER_NOT_MATCH
                )
                .verify();
    }

    @Test
    void signup_expiredToken() {
        UserRequestDTO dto = new UserRequestDTO(
                "alice@example.com", "password", "dongjun",
                "nick", "emailaa", "01022222222", "token123"
        );
        when(valueOperations.get("token123"))
                .thenReturn(Mono.empty());

        StepVerifier.create(authService.signup(Mono.just(dto)))
                .expectErrorMatches(ex ->
                        ((BusinessException) ex).getErrorCode() == ErrorCode.EXPIRED_TOKEN
                )
                .verify();
    }

    @Test
    void changePassword_userNotFound() {
        when(userRepository.findByUserId("alice"))
                .thenReturn(Mono.empty());

        StepVerifier.create(authService.changePassword("alice", "old", "new"))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException
                                && ((BusinessException) ex).getErrorCode() == ErrorCode.USER_NOT_FOUND
                )
                .verify();
    }

    @Test
    void changePassword_passwordNotMatch() {
        User user = new User();
        user.setUserId("bob");
        user.setPassword("encodedOld");

        when(userRepository.findByUserId("bob"))
                .thenReturn(Mono.just(user));
        when(passwordEncoder.matches("wrongOld", "encodedOld"))
                .thenReturn(false);

        StepVerifier.create(authService.changePassword("bob", "wrongOld", "newPwd"))
                .expectErrorMatches(ex ->
                        ((BusinessException) ex).getErrorCode() == ErrorCode.PASSWORD_NOT_MATCH
                )
                .verify();
    }

    @Test
    void changePassword_success() {
        User user = new User();
        user.setUserId("bob");
        user.setPassword("encodedOld");
        user.setPasswordChangedAt(LocalDateTime.now().minusYears(1));

        when(userRepository.findByUserId("bob"))
                .thenReturn(Mono.just(user));
        when(passwordEncoder.matches("oldPwd", "encodedOld"))
                .thenReturn(true);
        when(passwordEncoder.encode("newPwd"))
                .thenReturn("encodedNew");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(captor.capture()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));
        when(jwtTokenProvider.createToken("bob"))
                .thenReturn("testJwtToken");

        StepVerifier.create(authService.changePassword("bob", "oldPwd", "newPwd"))
                .assertNext(dto -> {
                    assert dto.getToken().equals("testJwtToken");
                    User saved = captor.getValue();
                    assert saved.getPassword().equals("encodedNew");
                    assert saved.getPasswordChangedAt()
                            .isAfter(LocalDateTime.now().minusSeconds(1));
                })
                .verifyComplete();
    }

    @Test
    void findUserId_success() {
        String email = "alice@example.com";
        String code = "12345";
        String userId = "alice123";
        String key = REDIS_KEY_FIND_ID + email;

        when(hashOperations.entries(key))
                .thenReturn(Flux.just(Map.entry("code", code), Map.entry("userId", userId)));

        StepVerifier.create(authService.verifyFindUserIdCode(email, code))
                .expectNext(userId)
                .verifyComplete();
    }

    @Test
    void findUserId_fail() {
        String email = "alice@example.com";
        String code = "12345";
        String userId = "alice123";
        String key = REDIS_KEY_FIND_ID + email;

        when(hashOperations.entries(key))
                .thenReturn(Flux.just(Map.entry("code", code), Map.entry("userId", userId)));

        StepVerifier.create(authService.verifyFindUserIdCode(email, "45678"))
                .expectErrorMatches(ex ->
                        ((BusinessException) ex).getErrorCode() == ErrorCode.AUTH_NUMBER_NOT_MATCH
                )
                .verify();
    }
}
