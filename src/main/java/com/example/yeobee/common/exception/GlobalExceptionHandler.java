package com.example.yeobee.common.exception;

import com.example.yeobee.common.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponseDto> handleBusinessException(
        final BusinessException e,
        final HttpServletRequest request
    ) {
        log.error("BusinessException: {} {}", e.getErrorCode(), request.getRequestURL());
        return ResponseEntity
            .status(e.getErrorCode().getHttpStatus().value())
            .body(new ErrorResponseDto(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
        final MethodArgumentNotValidException e,
        final HttpServletRequest request
    ) {
        log.error("MethodArgumentNotValidException: {} {}", e.getMessage(), request.getRequestURL());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode(),
                                                                 e.getAllErrors().get(0).getDefaultMessage());

        return ResponseEntity
            .status(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getHttpStatus().value())
            .body(errorResponseDto);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodNotSupportException(
        final HttpRequestMethodNotSupportedException e
    ) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
            ErrorCode.METHOD_NOT_ALLOWED.getCode(),
            e.getMessage()
        );

        return ResponseEntity
            .status(ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus().value())
            .body(errorResponseDto);
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class, SQLException.class,
        DataIntegrityViolationException.class})
    protected ResponseEntity<ErrorResponseDto> handleInternalException(
        final Exception e,
        final HttpServletRequest request
    ) {
        log.error("Exception: {} {}", e.getMessage(), request.getRequestURL());
        return ResponseEntity
            .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())
            .body(new ErrorResponseDto(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
