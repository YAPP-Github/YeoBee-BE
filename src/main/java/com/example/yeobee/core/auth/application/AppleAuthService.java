package com.example.yeobee.core.auth.application;

import com.example.yeobee.core.auth.dto.response.AppleAuthTokenResponseDto;
import com.example.yeobee.core.auth.util.Base64Decoder;
import com.example.yeobee.core.user.domain.AuthProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AppleAuthService {

    private static final String OAUTH_TOKEN_ENDPOINT = "https://appleid.apple.com/auth/oauth2/v2/token";
    private static final String OAUTH_UNLINK_ENDPOINT = "https://appleid.apple.com/auth/oauth2/v2/revoke";

    private final AppleAuthProperties appleAuthProperties;
    private final RestTemplate restTemplate;

    public void revoke(AuthProvider authProvider) {
        String appleRefreshToken = authProvider.getAppleRefreshToken();
        String clientSecret = this.createClientSecret();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", appleAuthProperties.clientId());
        params.add("client_secret", clientSecret);
        params.add("token", appleRefreshToken);
        params.add("token_type_hint", "refresh_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        restTemplate.postForEntity(OAUTH_UNLINK_ENDPOINT, httpEntity, AppleAuthTokenResponseDto.class);
    }

    public AppleAuthTokenResponseDto requestAppleAuthToken(String code) {
        String clientSecret = createClientSecret();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", appleAuthProperties.clientId());
        params.add("client_secret", clientSecret);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<AppleAuthTokenResponseDto> response = restTemplate.postForEntity(OAUTH_TOKEN_ENDPOINT,
                                                                                        httpEntity,
                                                                                        AppleAuthTokenResponseDto.class);
        return response.getBody();
    }

    private String createClientSecret() {
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(30).toInstant());
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("kid", appleAuthProperties.keyId());
        jwtHeader.put("alg", "ES256");
        return Jwts.builder()
            .setHeaderParams(jwtHeader)
            .setIssuer(appleAuthProperties.teamId())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(expirationDate)
            .setAudience("https://appleid.apple.com")
            .setSubject(appleAuthProperties.clientId())
            .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
            .compact();
    }

    @SneakyThrows
    private PrivateKey getPrivateKey() {
        String authKey = Base64Decoder.decode(appleAuthProperties.authKey());
        Reader pemReader = new StringReader(authKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }
}
