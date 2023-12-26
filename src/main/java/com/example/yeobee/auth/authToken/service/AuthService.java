package com.example.yeobee.auth.authToken.service;

import com.example.yeobee.auth.authToken.dto.response.AuthResponseDto;
import com.example.yeobee.auth.jwt.authToken.AuthToken;
import com.example.yeobee.auth.jwt.provider.AuthTokenProvider;
import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.domain.user.entity.AuthProvider;
import com.example.yeobee.domain.user.entity.LoginProvider;
import com.example.yeobee.domain.user.entity.RefreshToken;
import com.example.yeobee.domain.user.entity.User;
import com.example.yeobee.domain.user.repository.AuthProviderRepository;
import com.example.yeobee.domain.user.repository.RefreshTokenRepository;
import com.example.yeobee.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;
    private final AuthTokenProvider authTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponseDto refreshToken(String refreshToken) throws BusinessException {
        AuthToken authToken = authTokenProvider.convertAuthToken(refreshToken);
        if (!authToken.validate()) {
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        }
        String userId = authToken.getTokenSubject();
        RefreshToken retrievedRefreshToken = refreshTokenRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));
        if (!retrievedRefreshToken.getRefreshToken().equals(refreshToken)) {
            throw new BusinessException(ErrorCode.INCORRECT_REFRESH_TOKEN);
        }
        AuthToken newAuthToken = authTokenProvider.createUserAppToken(userId);
        return new AuthResponseDto(newAuthToken.getToken(), refreshToken);
    }

    public AuthProvider login(String socialLoginId, LoginProvider loginProvider) {
        AuthProvider authProvider = authProviderRepository.findBySocialLoginId(socialLoginId)
            .orElse(AuthProvider.builder().socialLoginId(socialLoginId).loginProvider(loginProvider).build());
        if (authProvider.getUser() == null) {
            User user = new User();
            user.addAuthProvider(authProvider);
            userRepository.save(user);
        }
        return authProvider;
    }

    public AuthResponseDto issueToken(User user) {
        AuthToken refreshToken = authTokenProvider.createRefreshToken(user.getId());
        AuthToken appToken = authTokenProvider.createUserAppToken(user.getId());
        refreshTokenRepository.save(RefreshToken.builder()
                                        .userId(user.getId())
                                        .refreshToken(refreshToken.getToken())
                                        .build());
        return AuthResponseDto.builder()
            .appToken(appToken.getToken())
            .refreshToken(refreshToken.getToken())
            .build();
    }
}
