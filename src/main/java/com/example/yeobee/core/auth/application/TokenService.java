package com.example.yeobee.core.auth.application;

import com.example.yeobee.core.auth.domain.AuthToken;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenService {

    private final JwtProperties jwtProperties;

    public AuthToken createAccessToken(long id) {
        return new AuthToken(id,
                             Long.parseLong(jwtProperties.accessTokenExpiry()),
                             generateShaKey(jwtProperties.secretKey()));
    }

    public AuthToken createRefreshToken(long id) {
        return new AuthToken(id,
                             Long.parseLong(jwtProperties.refreshTokenExpiry()),
                             generateShaKey(jwtProperties.secretKey()));
    }

    public AuthToken convertToken(String token) {
        return new AuthToken(token, generateShaKey(jwtProperties.secretKey()));
    }

    private Key generateShaKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}

