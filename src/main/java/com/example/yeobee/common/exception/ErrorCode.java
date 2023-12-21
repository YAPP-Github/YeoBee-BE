package com.example.yeobee.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /**
     * 1XXX -> Common 에러
     */
    BAD_REQUEST(1000, "Bad Request", HttpStatus.BAD_REQUEST),
    NOT_FOUND(1001, "Contents Not Found", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(1002, "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_SERVER_ERROR(1003, "Internal Server Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_ARGUMENT_NOT_VALID(1004, "Method Argument Is Not Valid", HttpStatus.BAD_REQUEST),
    /**
     * 2XXX -> JWT 에러
     */
    JWT_EMPTY(2000, "Access Token Is Empty", HttpStatus.UNAUTHORIZED),
    JWT_EXPIRED(2001, "Access Token has expired", HttpStatus.UNAUTHORIZED),
    INVALID_JWT_TOKEN(2002, "Access Token Is Invalid", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(2003, "Refresh Token Is Invalid", HttpStatus.UNAUTHORIZED),
    INCORRECT_REFRESH_TOKEN(2004, "Refresh Token Mismatched With User's Refresh Token", HttpStatus.UNAUTHORIZED),
    /**
     * 3XXX -> User 에러
     */
    USER_NOT_FOUND(2004, "User Not found", HttpStatus.NOT_FOUND);


    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
