package com.example.yeobee.core.auth.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.auth.domain.*;
import com.example.yeobee.core.auth.dto.request.AppleLoginRequestDto;
import com.example.yeobee.core.auth.dto.request.KakaoLoginRequestDto;
import com.example.yeobee.core.auth.dto.response.TokenResponseDto;
import com.example.yeobee.core.auth.util.JwtParser;
import com.example.yeobee.core.user.domain.User;
import com.example.yeobee.core.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProviderRepository authProviderRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AppleAuthService appleAuthService;
    private final KakaoAuthService kakaoAuthService;

    @Transactional
    public TokenResponseDto login(AppleLoginRequestDto appleLoginRequest) {
        String appleRefreshToken = appleAuthService.requestAppleAuthToken(appleLoginRequest.code()).refreshToken();
        String socialLoginId = JwtParser.getSocialIdFromJwt(appleLoginRequest.idToken());
        AuthProvider authProvider = getOrCreateAuthProvider(socialLoginId, AuthProviderType.APPLE);
        authProvider.setAppleRefreshToken(appleRefreshToken);
        authProviderRepository.save(authProvider);
        return issueToken(authProvider.getUser());
    }

    public TokenResponseDto login(KakaoLoginRequestDto kakaoLoginRequest) {
        String socialLoginId = kakaoAuthService.getSocialLoginId(kakaoLoginRequest.oauthToken());
        AuthProvider authProvider = getOrCreateAuthProvider(socialLoginId, AuthProviderType.KAKAO);
        return issueToken(authProvider.getUser());
    }

    public TokenResponseDto refreshToken(String refreshToken) {
        AuthToken authToken = tokenService.convertToken(refreshToken);
        long userId = Long.parseLong(authToken.getClaims().getSubject());

        RefreshToken retrievedRefreshToken = refreshTokenRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));
        if (!retrievedRefreshToken.getValue().equals(refreshToken)) {
            throw new BusinessException(ErrorCode.INCORRECT_REFRESH_TOKEN);
        }

        AuthToken newAuthToken = tokenService.createAccessToken(userId);
        return new TokenResponseDto(newAuthToken.getToken(), refreshToken);
    }

    @Transactional
    public AuthProvider getOrCreateAuthProvider(String socialLoginId, AuthProviderType authProviderType) {
        AuthProvider authProvider = authProviderRepository.findBySocialLoginId(socialLoginId)
            .orElse(new AuthProvider(socialLoginId, authProviderType));

        if (authProvider.getUser() == null) {
            User user = new User(authProvider);
            userRepository.save(user);
        }

        return authProvider;
    }

    public void logout(User user) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findById(user.getId());
        optionalRefreshToken.ifPresent(refreshTokenRepository::delete);
    }

    public TokenResponseDto issueToken(User user) {
        AuthToken refreshToken = tokenService.createRefreshToken(user.getId());
        AuthToken accessToken = tokenService.createAccessToken(user.getId());
        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken.getToken()));

        return new TokenResponseDto(accessToken.getToken(), refreshToken.getToken());
    }

    public User getUserByToken(String tokenStr) {
        AuthToken authToken = tokenService.convertToken(tokenStr);
        long userId = Long.parseLong(authToken.getClaims().getSubject());
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void deleteUser(User user) {
        AuthProvider authProvider = user.getAuthProvider();
        switch (authProvider.getType()) {
            case APPLE -> appleAuthService.revoke(authProvider);
            case KAKAO -> kakaoAuthService.revoke(authProvider);
            default -> throw new BusinessException(ErrorCode.AUTH_PROVIDER_TYPE_INVALID);
        }
        authProviderRepository.delete(authProvider);
        userRepository.delete(user);
    }
}
