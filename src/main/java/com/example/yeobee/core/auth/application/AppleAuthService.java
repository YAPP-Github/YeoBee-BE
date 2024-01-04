package com.example.yeobee.core.auth.application;

import com.example.yeobee.common.util.JwtParser;
import com.example.yeobee.core.auth.domain.AuthProvider;
import com.example.yeobee.core.auth.domain.AuthProviderRepository;
import com.example.yeobee.core.auth.domain.AuthProviderType;
import com.example.yeobee.core.auth.dto.request.AppleLoginRequestDto;
import com.example.yeobee.core.auth.dto.response.AppleAuthTokenResponseDto;
import com.example.yeobee.core.auth.dto.response.TokenResponseDto;
import com.example.yeobee.core.user.domain.User;
import com.example.yeobee.core.user.domain.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AppleAuthService {

    private static final String OAUTH_ENDPOINT = "https://appleid.apple.com/auth/oauth2/v2/token";

    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;
    private final AuthService authService;
    private final AppleAuthProperties appleAuthProperties;
    private final RestTemplate restTemplate;

    public void unlinkUser(User user) {
        String appleRefreshToken = user.getAuthProviderList()
            .stream()
            .filter((e) -> e.getType() == AuthProviderType.APPLE)
            .toList()
            .get(0)
            .getAppleRefreshToken();
        userRepository.delete(user);
        String clientSecret = this.createClientSecret();
        String authUrl = "https://appleid.apple.com/auth/oauth2/v2/revoke";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", appleAuthProperties.clientId());
        params.add("client_secret", clientSecret);
        params.add("token", appleRefreshToken);
        params.add("token_type_hint", "refresh_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        restTemplate.postForEntity(authUrl, httpEntity, AppleAuthTokenResponseDto.class);
    }

    @Transactional
    public TokenResponseDto login(AppleLoginRequestDto appleLoginRequest) {
        String appleRefreshToken = requestAppleAuthToken(appleLoginRequest.code(),
                                                         createClientSecret()).refreshToken();
        String socialLoginId = JwtParser.getSocialIdFromJwt(appleLoginRequest.idToken());
        AuthProvider authProvider = authService.login(socialLoginId, AuthProviderType.APPLE);
        authProvider.setAppleRefreshToken(appleRefreshToken);
        authProviderRepository.save(authProvider);
        return authService.issueToken(authProvider.getUser());
    }

    private AppleAuthTokenResponseDto requestAppleAuthToken(String code, String client_secret) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", appleAuthProperties.clientId());
        params.add("client_secret", client_secret);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<AppleAuthTokenResponseDto> response = restTemplate.postForEntity(OAUTH_ENDPOINT,
                                                                                        httpEntity,
                                                                                        AppleAuthTokenResponseDto.class);
        return response.getBody();
    }

    private String createClientSecret() {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
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
        ClassPathResource resource = new ClassPathResource("static/AuthKey_" + appleAuthProperties.keyId() + ".p8");
        String privateKey = new String(resource.getInputStream().readAllBytes());
        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }
}
