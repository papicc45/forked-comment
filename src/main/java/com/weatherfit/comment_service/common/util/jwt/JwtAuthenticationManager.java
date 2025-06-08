package com.weatherfit.comment_service.common.util.jwt;

import com.weatherfit.comment_service.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
@AllArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = String.valueOf(((BearerTokenAuthentication) authentication).getToken());
        if(!jwtTokenProvider.validateToken(token)) {
            return Mono.empty();
        }

        String userId = jwtTokenProvider.getUserIdFromToken(token);
        return userRepository.findByUserId(userId)
                .map(user -> {
                    return new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                });
    }
}
