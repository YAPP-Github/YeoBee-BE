package com.example.yeobee.core.auth.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.auth.domain.AuthProvider;
import com.example.yeobee.core.auth.domain.AuthProviderType;
import com.example.yeobee.core.auth.dto.request.KakaoLoginRequestDto;
import com.example.yeobee.core.auth.dto.response.KakaoUserResponseDto;
import com.example.yeobee.core.auth.dto.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private static final String OAUTH_ENDPOINT = "https://kapi.kakao.com/v2/user/me";
    private static final String OAUTH_UNLINK_ENDPOINT = "https://kapi.kakao.com/v1/user/unlink";

    private final KakaoAuthProperties kakaoAuthProperties;
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

    public void revoke(AuthProvider authProvider) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + kakaoAuthProperties.adminKey());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("target_id_type", "user_id");
        map.add("target_id", authProvider.getSocialLoginId());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        restTemplate.exchange(
            OAUTH_UNLINK_ENDPOINT,
            HttpMethod.POST,
            request,
            String.class);
    }
}
