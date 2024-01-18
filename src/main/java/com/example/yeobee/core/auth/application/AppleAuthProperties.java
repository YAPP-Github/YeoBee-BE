package com.example.yeobee.core.auth.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.apple")
public record AppleAuthProperties(String teamId, String keyId, String clientId, String authKey) {

}
