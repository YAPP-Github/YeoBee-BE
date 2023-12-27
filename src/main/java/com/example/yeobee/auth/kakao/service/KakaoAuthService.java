package com.example.yeobee.auth.kakao.service;

import com.example.yeobee.auth.authToken.dto.response.AuthResponseDto;
import com.example.yeobee.auth.authToken.service.AuthService;
import com.example.yeobee.auth.kakao.dto.KakaoLoginRequestDto;
import com.example.yeobee.auth.kakao.dto.KakaoUserResponseDto;
import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.domain.user.entity.AuthProvider;
import com.example.yeobee.domain.user.entity.LoginProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final AuthService authService;

    public AuthResponseDto login(KakaoLoginRequestDto loginRequest) {
        String socialLoginId = getSocialLoginId(loginRequest.oauthToken());
        AuthProvider authProvider = authService.login(socialLoginId, LoginProvider.KAKAO);
        return authService.issueToken(authProvider.getUser());
    }

    public String getSocialLoginId(String oauthToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(oauthToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserResponseDto> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
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
