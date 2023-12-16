package com.example.yeobee.auth.apple.service;

import com.example.yeobee.auth.apple.dto.request.AppleLoginRequestDto;
import com.example.yeobee.auth.apple.dto.response.AppleAuthTokenResponseDto;
import com.example.yeobee.auth.authToken.dto.response.AuthResponseDto;
import com.example.yeobee.auth.jwt.authToken.AuthToken;
import com.example.yeobee.auth.jwt.provider.AuthTokenProvider;
import com.example.yeobee.common.util.parser.JwtParser;
import com.example.yeobee.domain.user.entity.LoginProvider;
import com.example.yeobee.domain.user.entity.RefreshToken;
import com.example.yeobee.domain.user.entity.RoleType;
import com.example.yeobee.domain.user.repository.RefreshTokenRepository;
import com.example.yeobee.domain.user.repository.UserRepository;
import com.example.yeobee.domain.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppleAuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthTokenProvider authTokenProvider;
    @Value("${app.auth.keyId}")
    private String appleSignKeyId;
    @Value("${app.auth.teamId}")
    private String appleTeamId;
    @Value("${app.auth.appleKeyId}")
    private String keyId;
    @Value("${app.auth.appleClientId}")
    private String clientId;

    public AuthResponseDto login(AppleLoginRequestDto appleLoginRequest) throws ParseException, IOException {
        String clientSecret = this.createClientSecret();
        AppleAuthTokenResponseDto response = this.GenerateAuthToken(appleLoginRequest.getCode(), clientSecret);
        String appleRefreshToken = response.getRefresh_token();
        String socialLoginId = JwtParser.getSocialIdFromJwt(appleLoginRequest.getId_token());
        User user = userRepository.findBySocialLoginId(socialLoginId)
                .orElse(User.builder()
                .socialLoginId(socialLoginId)
                .roleType(RoleType.USER)
                .loginProvider(LoginProvider.APPLE)
                .build());
        user.setAppleRefreshToken(appleRefreshToken);
        userRepository.save(user);
        User savedUser = userRepository.save(user);
        AuthToken refreshToken = authTokenProvider.createRefreshToken(savedUser.getId());
        AuthToken appToken = authTokenProvider.createUserAppToken(savedUser.getId(), RoleType.USER);
        refreshTokenRepository.save(RefreshToken.builder().userId(savedUser.getId()).refreshToken(refreshToken.getToken()).build());
        return AuthResponseDto.builder()
                .appToken(appToken.getToken())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public void unlinkUser(User user) throws IOException {
        String appleRefreshToken = user.getAppleRefreshToken();
        userRepository.delete(user);
        String clientSecret = this.createClientSecret();

        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String authUrl = "https://appleid.apple.com/auth/oauth2/v2/revoke";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("token", appleRefreshToken);
        params.add("token_type_hint", "refresh_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        restTemplate.postForEntity(authUrl, httpEntity, AppleAuthTokenResponseDto.class);
    }

    private String createClientSecret() throws IOException {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("kid", appleSignKeyId);
        jwtHeader.put("alg", "ES256");

        return Jwts.builder()
                .setHeaderParams(jwtHeader)
                .setIssuer(appleTeamId)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 - UNIX 시간
                .setExpiration(expirationDate) // 만료 시간
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/AuthKey_" + keyId + ".p8");
        String privateKey = new String(resource.getInputStream().readAllBytes());
        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }

    public AppleAuthTokenResponseDto GenerateAuthToken(String code, String client_secret) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String authUrl = "https://appleid.apple.com/auth/oauth2/v2/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", client_secret);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<AppleAuthTokenResponseDto> response = restTemplate.postForEntity(authUrl, httpEntity, AppleAuthTokenResponseDto.class);
        return response.getBody();
    }

}
