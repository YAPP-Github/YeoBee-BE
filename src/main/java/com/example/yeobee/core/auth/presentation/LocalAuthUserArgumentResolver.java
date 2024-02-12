package com.example.yeobee.core.auth.presentation;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.auth.application.AuthService;
import com.example.yeobee.core.user.domain.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Profile("!prod")
@Component
public class LocalAuthUserArgumentResolver extends AuthUserArgumentResolver {

    private final UserRepository userRepository;

    public LocalAuthUserArgumentResolver(AuthService authService, UserRepository userRepository) {
        super(authService);
        this.userRepository = userRepository;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        long id = Long.parseLong(request.getHeader("Authorization").substring(7));
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}

