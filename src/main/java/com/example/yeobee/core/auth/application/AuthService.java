package com.example.yeobee.core.auth.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.auth.domain.*;
import com.example.yeobee.core.auth.dto.response.TokenResponseDto;
import com.example.yeobee.core.user.entity.User;
import com.example.yeobee.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProviderRepository authProviderRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

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

    public AuthProvider login(String socialLoginId, AuthProviderType authProviderType) {
        AuthProvider authProvider = authProviderRepository.findBySocialLoginId(socialLoginId)
            .orElse(new AuthProvider(socialLoginId, authProviderType));

        if (authProvider.getUser() == null) {
            User user = new User(authProvider);
            userRepository.save(user);
        }

        return authProvider;
    }

    public void logout(User user){
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
}
