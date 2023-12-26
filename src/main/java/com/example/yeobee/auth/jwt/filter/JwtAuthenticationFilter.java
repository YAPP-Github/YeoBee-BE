package com.example.yeobee.auth.jwt.filter;

import com.example.yeobee.auth.jwt.authToken.AuthToken;
import com.example.yeobee.auth.jwt.provider.AuthTokenProvider;
import com.example.yeobee.auth.jwt.securityUserDetails.SecurityUserDetailsService;
import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.common.util.jwt.JwtHeaderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private AuthTokenProvider tokenProvider;
    private SecurityUserDetailsService securityUserDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String tokenStr = JwtHeaderUtil.getAccessToken(request);
            AuthToken token = tokenProvider.convertAuthToken(tokenStr);
            try {
                if (token.validate()) {
                    UserDetails userDetails = securityUserDetailsService.loadUserByUserId(token.getTokenClaims()
                                                                                              .getSubject());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (BusinessException e) {
                setResponse(response, e.getErrorCode());
                return;
            }
            filterChain.doFilter(request, response);
        } else {
            setResponse(response, ErrorCode.JWT_EMPTY);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("message", errorCode.getMessage());
        hashMap.put("code", Integer.toString(errorCode.getCode()));
        JSONObject responseJson = new JSONObject(hashMap);
        response.getWriter().print(responseJson);
    }
}