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
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

/** ExtendWith - Junit5에 Mockito 기능 통합, @Mock과 @InjectMocks 활성화*/
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    ReactiveStringRedisTemplate redisOperations;

    @Mock
    private UserMapper userMapper;

    @Mock
    ReactiveValueOperations<String, String> valueOperations;

    private static final String REDIS_KEY_TOKEN = "email:sign:";

    /** 이 객체 생성할 때, 위 목 객체들 주입*/
    @InjectMocks
    AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        given(redisOperations.opsForValue()).willReturn(valueOperations);
    }

    @Test
    void signup_success() {
        UserRequestDTO dto = new UserRequestDTO("alice@example.com", "password", "nick", "emailaa", "01022222222", "token456");
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setEmail(dto.getEmail());
        user.setPassword("raw");

        LocalDateTime now  = LocalDateTime.now();
        ReflectionTestUtils.setField(user, "createdDate", now);
        ReflectionTestUtils.setField(user, "modifiedDate", now);

        given(valueOperations.get("token456")).willReturn(Mono.just("true"));
        given(userMapper.DTOToUser(dto)).willReturn(user);
        given(passwordEncoder.encode("password")).willReturn("encodedPwd");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        given(userRepository.save(captor.capture()))
                .willAnswer(inv -> Mono.just(captor.getValue()));

        given(redisOperations.opsForValue()).willReturn(valueOperations);
        given(valueOperations.delete(anyString())).willReturn(Mono.just(true));

        UserResponseDTO responseDTO = new UserResponseDTO(user.getId(), user.getUserId(), user.getNickname(), user.getCreatedDate().toString(), user.getModifiedDate().toString());
        given(userMapper.userToDTO(user)).willReturn(responseDTO);

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
        UserRequestDTO dto = new UserRequestDTO("alice@example.com", "password", "nick", "emailaa", "01022222222", "token456");
        given(valueOperations.get("token456")).willReturn(Mono.just("false"));

        StepVerifier.create(authService.signup(Mono.just(dto)))
                .expectErrorMatches(ex -> ((BusinessException) ex).getErrorCode() == ErrorCode.AUTH_NUMBER_NOT_MATCH)
                .verify();
    }

    @Test
    void signup_expiredToken() {
        UserRequestDTO dto = new UserRequestDTO("alice@example.com", "password", "nick", "emailaa", "01022222222", "token123");
        given(valueOperations.get("token123")).willReturn(Mono.empty());

        StepVerifier.create(authService.signup(Mono.just(dto)))
                .expectErrorMatches(ex -> ((BusinessException) ex).getErrorCode() == ErrorCode.EXPIRED_TOKEN)
                .verify();
    }
    @Test
    void changePassword_userNotFound() {

        given(userRepository.findByUserId("alice"))
                .willReturn(Mono.empty());
        /** StepVerifier -> Mono나 Flux에 대해 시퀀스 정의, 기대값 검증 */
        StepVerifier.create(authService.changePassword("alice", "old", "new"))
                .expectErrorMatches(ex -> ex instanceof BusinessException && ((BusinessException) ex).getErrorCode() == ErrorCode.USER_NOT_FOUND)
                .verify();
    }

    @Test
    void changePassword_passwordNotMatch() {

        User user = new User();
        user.setUserId("bob");
        user.setPassword("encodedOld");

        given(userRepository.findByUserId("bob"))
                .willReturn(Mono.just(user));

        given(passwordEncoder.matches("wrongOld", "encodedOld"))
                .willReturn(false);

        StepVerifier.create(authService.changePassword("bob", "wrongOld", "newPwd"))
                .expectErrorMatches(ex -> ((BusinessException) ex).getErrorCode() == ErrorCode.PASSWORD_NOT_MATCH)
                .verify();
    }

    @Test
    void changePassword_Success() {
        User user = new User();
        user.setUserId("bob");
        user.setPassword("encodedOld");
        user.setPasswordChangedAt(LocalDateTime.now().minusYears(1));

        given(userRepository.findByUserId("bob"))
                .willReturn(Mono.just(user));
        given(passwordEncoder.matches("oldPwd", "encodedOld"))
                .willReturn(true);
        given(passwordEncoder.encode("newPwd"))
                .willReturn("encodedNew");

        /** ArgumentCaptor -> 목 객체 호출시 전달된 파라미터를 캡처해서 나중에 검증하는 역할*/
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        given(userRepository.save(captor.capture())) /** capture() -> save() 호출 시 넘어온 User 인스턴스를 captor 객체에 저장*/
                .willAnswer(inv -> Mono.just(inv.getArgument(0))); /** 첫번째 파라미터 꺼내옴*/
        given(jwtTokenProvider.createToken("bob"))
                .willReturn("testJwtToken");

        StepVerifier.create(authService.changePassword("bob", "oldPwd", "newPwd"))
                .assertNext(dto -> {
                    assert dto.getToken().equals("testJwtToken");
                    User saved = captor.getValue();
                    assert saved.getPassword().equals("encodedNew");
                    assert saved.getPasswordChangedAt().isAfter(LocalDateTime.now().minusSeconds(1));
                })
                .verifyComplete();
    }
}
