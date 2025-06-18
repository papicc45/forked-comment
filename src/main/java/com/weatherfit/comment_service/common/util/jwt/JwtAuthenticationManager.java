package com.weatherfit.comment_service.common.util.jwt;

import com.weatherfit.comment_service.common.exception.BusinessException;
import com.weatherfit.comment_service.common.exception.ErrorCode;
import com.weatherfit.comment_service.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

@Component
@AllArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    /** authentication 파라미터 - AuthorizationWebFilter 단계에서 생성된 Bearer 토큰 인스턴스*/
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = ((BearerTokenAuthenticationToken) authentication).getToken();

        /** 토큰 검증, 유효하지 않으면 인증 실패 처리*/
        if(!jwtTokenProvider.validateToken(token)) {
            return Mono.empty();
        }

        /** 사용자 존재 여부 확인*/
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        return userRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND)))
                .flatMap(user -> {
                    Date issuedAt = jwtTokenProvider.getIssuedAt(token);
                    Instant pwdChanged = user.getPasswordChangedAt().atZone(ZoneId.systemDefault()).toInstant();

                    if(issuedAt.toInstant().isBefore(pwdChanged)) {
                        return Mono.error(new BusinessException(ErrorCode.EXPIRED_TOKEN));
                    }

                    return Mono.just(new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList()));
                });
    }
}
