package com.weatherfit.comment_service.user;

import com.weatherfit.comment_service.auth.service.AuthServiceImpl;
import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.user.entity.User;
import com.weatherfit.comment_service.user.repository.UserRepository;
import com.weatherfit.comment_service.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.convert.GeoIndexedPropertyValue;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock private UserRepository userRepository;
    @Mock private AuthServiceImpl authService;
    @Mock private ReactiveRedisOperations<String,String> reactiveRedisOperations;
    @Mock private ReactiveValueOperations<String,String> valueOperations;
    @Mock private ReactiveHashOperations<String,String,String> hashOperations;
    @InjectMocks private UserServiceImpl userService;

    private static final String REDIS_KEY_FIND_ID = "FIND_ID_CODE:";

    @BeforeEach
    void setUp() {
        lenient().when(reactiveRedisOperations.<String, String>opsForHash())
                .thenReturn(hashOperations);
    }

    @Test
    void 아이디찾기_success() {
        User user = new User();
        user.setName("이동준");
        user.setEmail("papicc45@naver.com");
        user.setUserId("papicc45");

        when(userRepository.findByNicknameAndEmail("이동준", "papicc45@naver.com"))
                .thenReturn(Mono.just(user));

        when(authService.generateRandomCode(15))
                .thenReturn("123456789012345");

        when(hashOperations.putAll(
                eq(REDIS_KEY_FIND_ID + user.getEmail()),
                eq(Map.of("code", "123456789012345", "userId", user.getUserId())))
        ).thenReturn(Mono.just(true));

        when(reactiveRedisOperations.expire(
                eq(REDIS_KEY_FIND_ID + user.getEmail()),
                eq(Duration.ofMinutes(5))
        )).thenReturn(Mono.just(true));

        when(authService.sendCodeEmail(user.getEmail(), "123456789012345", 2))
                .thenReturn(Mono.empty());

        StepVerifier.create(userService.checkUserNameAndEmailMatch("이동준", "papicc45@naver.com"))
                .verifyComplete();

        /** Inorder - 호출 순서 검증 */
        InOrder order = inOrder(userRepository, authService, hashOperations, reactiveRedisOperations);
        order.verify(userRepository).findByNicknameAndEmail(user.getName(), user.getEmail());
        order.verify(authService).generateRandomCode(15);
        order.verify(hashOperations).putAll(eq(REDIS_KEY_FIND_ID + user.getEmail()), eq(Map.of("code", "123456789012345", "userId", user.getUserId())));
        order.verify(reactiveRedisOperations).expire(eq(REDIS_KEY_FIND_ID + user.getEmail()), eq(Duration.ofMinutes(5)));
        order.verify(authService).sendCodeEmail(user.getEmail(), "123456789012345", 2);
    }

    @Test
    void 아이디찾기_인증과정실패() {
        User user = new User();
        user.setName("이동준");
        user.setEmail("papicc45@naver.com");
        user.setUserId("papicc45");

        when(userRepository.findByNicknameAndEmail("이동", "papicc45@naver.com"))
                .thenReturn(Mono.empty());

        StepVerifier.create(userService.checkUserNameAndEmailMatch("이동", "papicc45@naver.com"))
                .expectErrorMatches(ex ->
                                ((BusinessException) ex).getErrorCode() == ErrorCode.USER_NOT_FOUND
                        )
                .verify();
    }
}
