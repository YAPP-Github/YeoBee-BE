package com.example.yeobee.core.auth.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.auth.domain.AuthProvider;
import com.example.yeobee.core.auth.domain.AuthProviderType;
import com.example.yeobee.core.auth.dto.request.KakaoLoginRequestDto;
import com.example.yeobee.core.auth.dto.response.KakaoUserResponseDto;
import com.example.yeobee.core.auth.dto.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private static final String OAUTH_ENDPOINT = "https://kapi.kakao.com/v2/user/me";

    private final AuthService authService;
    private final RestTemplate restTemplate;

    @Transactional
    public TokenResponseDto login(KakaoLoginRequestDto loginRequest) {
        String socialLoginId = getSocialLoginId(loginRequest.oauthToken());
        AuthProvider authProvider = authService.login(socialLoginId, AuthProviderType.KAKAO);
        return authService.issueToken(authProvider.getUser());
    }

    private String getSocialLoginId(String oauthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(oauthToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserResponseDto> response = restTemplate.exchange(
                OAUTH_ENDPOINT,
                HttpMethod.GET,
                entity,
                KakaoUserResponseDto.class);
            KakaoUserResponseDto kakaoUserResponse = response.getBody();
            if (kakaoUserResponse == null) {
                throw new BusinessException(ErrorCode.KAKAO_USER_NOT_FOUND);
            }
            return kakaoUserResponse.id().toString();
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ErrorCode.INVALID_KAKAO_AUTH_TOKEN);
        } catch (HttpServerErrorException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
