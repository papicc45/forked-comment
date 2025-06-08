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
    private final ServerBearerTokenAuthenticationConverter bearerConverter =
            new ServerBearerTokenAuthenticationConverter();
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return bearerConverter.convert(exchange)
                .flatMap(authManager::authenticate)
                .map(SecurityContextImpl::new);
    }
}
