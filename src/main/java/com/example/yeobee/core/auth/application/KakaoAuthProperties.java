package com.example.yeobee.core.auth.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoAuthProperties(String adminKey) {

}
