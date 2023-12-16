package com.example.yeobee.auth.jwt.authToken;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.domain.user.entity.RoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    @Getter
    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    public AuthToken(String socialId, RoleType roleType, Date expiry, Key key) {
        String role = roleType.getCode();
        this.key = key;
        this.token = createAuthToken(socialId, role, expiry);
    }
    private String createAuthToken(String socialId, String role, Date expiry) {
        return Jwts.builder()
                .setSubject(socialId)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }
    public AuthToken(String socialId, Date expiry, Key key) {
        this.key = key;
        this.token = createRefreshToken(socialId, expiry);
    }
    private String createRefreshToken(String subject, Date expiry){
        return Jwts.builder()
                .setSubject(subject)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }
    public boolean validate() throws BusinessException {
        return this.getTokenClaims() != null;
    }
    public Claims getTokenClaims() throws BusinessException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException | SignatureException e) {
            log.info("Invalid JWT Signature");
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new BusinessException(ErrorCode.JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public String getTokenSubject() throws BusinessException {
        return this.getTokenClaims().getSubject();
    }
}
