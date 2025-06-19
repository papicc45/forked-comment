package com.weatherfit.comment_service.auth;

import com.weatherfit.comment_service.auth.controller.AuthController;
import com.weatherfit.comment_service.auth.dto.JwtResponseDTO;
import com.weatherfit.comment_service.auth.service.AuthService;
import com.weatherfit.comment_service.common.config.SecurityConfig;
import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.common.repository.SecurityContextRepository;
import com.weatherfit.comment_service.common.util.jwt.JwtAuthenticationManager;
import com.weatherfit.comment_service.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.convert.GeoIndexedPropertyValue;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = {AuthController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class, ReactiveSecurityAutoConfiguration.class})
@AutoConfigureWebTestClient
public class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthService authService;

    @Test
    void changePassword_userNotFound() {
        given(authService.changePassword("uid", "oldPwd", "newPwd"))
                .willReturn(Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND)));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "uid");
        params.add("beforePwd", "oldPwd");
        params.add("afterPwd", "newPwd");

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/password")
                        .queryParams(params)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("사용자 존재하지 않습니다.");

    }
    @Test
    void changePassword_passwordNotMatch() {
        given(authService.changePassword("uid", "oldPwd", "newPwd"))
                .willReturn(Mono.error(new BusinessException(ErrorCode.PASSWORD_NOT_MATCH)));

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/password")
                        .queryParam("userId", "uid")
                        .queryParam("beforePwd", "oldPwd")
                        .queryParam("afterPwd", "newPwd")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("비밀번호가 일치하지 않습니다.");
    }

    @Test
    void changePassword_Success() {

        JwtResponseDTO resp = new JwtResponseDTO("testJwtToken", false);
        given(authService.changePassword("uid", "oldPwd", "newPwd"))
                .willReturn(Mono.just(resp));

        webTestClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/password")
                        .queryParam("userId", "uid")
                        .queryParam("beforePwd", "oldPwd")
                        .queryParam("afterPwd", "newPwd")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(JwtResponseDTO.class)
                .value(body -> {
                    assert body.getToken().equals("testJwtToken");
                    assert body.isRequirePasswordChange() == false;
                });
    }


}
