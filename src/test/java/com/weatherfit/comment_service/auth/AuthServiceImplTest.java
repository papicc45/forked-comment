package com.weatherfit.comment_service.auth;

import com.weatherfit.comment_service.auth.service.AuthServiceImpl;
import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.common.util.jwt.JwtTokenProvider;
import com.weatherfit.comment_service.user.entity.User;
import com.weatherfit.comment_service.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    /** 이 객체 생성할 때, 위 목 객체들 주입*/
    @InjectMocks
    AuthServiceImpl authService;

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
