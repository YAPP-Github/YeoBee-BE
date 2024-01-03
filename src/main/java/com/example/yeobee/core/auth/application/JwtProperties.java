package com.example.yeobee.core.auth.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.jwt")
public record JwtProperties(String secretKey, Long accessTokenExpiry, Long refreshTokenExpiry
) {

}
