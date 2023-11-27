package com.weatherfit.comment_service.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final String SECREY_KEY = "kdt8ProjectWeatherfitSecretKeykdt8ProjectWeatherfitSecretKeykdt8ProjectWeatherfitSecretKey";
    private static final long EXPIRATION_TIME = 86400000L;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder().setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECREY_KEY)
                .compact();
    }
}
