package com.example.yeobee.common.util;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;

public class AuthHeaderParser {

    public static String parseTokenString(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else {
            throw new BusinessException(ErrorCode.JWT_EMPTY);
        }
    }
}
