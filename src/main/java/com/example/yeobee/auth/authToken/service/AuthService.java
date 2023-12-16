package com.example.yeobee.auth.authToken.service;

import com.example.yeobee.auth.authToken.dto.response.AuthResponseDto;
import com.example.yeobee.auth.jwt.authToken.AuthToken;
import com.example.yeobee.auth.jwt.provider.AuthTokenProvider;
import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.domain.user.entity.RefreshToken;
import com.example.yeobee.domain.user.entity.User;
import com.example.yeobee.domain.user.repository.RefreshTokenRepository;
import com.example.yeobee.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthTokenProvider authTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    public AuthResponseDto refreshToken(String refreshToken) throws BusinessException {
        AuthToken authToken = authTokenProvider.convertAuthToken(refreshToken);
        if (!authToken.validate())
            throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
        String userId = authToken.getTokenSubject();
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));
        RefreshToken retrievedRefreshToken = refreshTokenRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));
        if (!retrievedRefreshToken.getRefreshToken().equals(refreshToken))
            throw new BusinessException(ErrorCode.INCORRECT_REFRESH_TOKEN);
        AuthToken newAuthToken = authTokenProvider.createUserAppToken(userId, user.getRoleType());
        return new AuthResponseDto(newAuthToken.getToken(), refreshToken);
    }
}
