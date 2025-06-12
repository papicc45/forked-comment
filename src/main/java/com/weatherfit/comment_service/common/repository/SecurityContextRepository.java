package com.weatherfit.comment_service.common.repository;

import com.weatherfit.comment_service.common.util.jwt.JwtAuthenticationManager;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.server.resource.web.server.authentication.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtAuthenticationManager authManager;
    /** http 요청의 Authorization 헤더 읽어 BearerTokenAuthenticationToken 객체로 변환*/
    private final ServerBearerTokenAuthenticationConverter bearerConverter =
            new ServerBearerTokenAuthenticationConverter();

    /** 토큰 기반 인증에선 인증결과 저장할 필요 없기에 빈 구현*/
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return bearerConverter.convert(exchange) /** HTTP 헤더에서 Authorization 값 꺼내 래핑*/
                .flatMap(authManager::authenticate) /** 토큰 유효성 검증, DB 조회 성공 시 Authentication 객체 반환*/
                .map(SecurityContextImpl::new); /** 최종 리턴*/
    }
}
