package com.example.yeobee.auth.jwt.resolver;

import com.example.yeobee.auth.jwt.annotation.AuthUser;
import com.example.yeobee.auth.jwt.authToken.AuthToken;
import com.example.yeobee.auth.jwt.provider.AuthTokenProvider;
import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthUser.class) != null;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String tokenStr = authHeader.substring(7);
            AuthToken token = tokenProvider.convertAuthToken(tokenStr);
            if (token.validate()) {
                return userRepository.findById(token.getTokenClaims().getSubject())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JWT_TOKEN));
            } else {
                throw new BusinessException(ErrorCode.INVALID_JWT_TOKEN);
            }
        } else {
            throw new BusinessException(ErrorCode.JWT_EMPTY);
        }
    }
}

