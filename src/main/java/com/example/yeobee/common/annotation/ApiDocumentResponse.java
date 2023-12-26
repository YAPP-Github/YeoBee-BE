package com.example.yeobee.common.annotation;

import com.example.yeobee.common.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "API 호출 성공"),
    @ApiResponse(
        responseCode = "401",
        description = """
            response 내의 code 확인
                        
            2000: 액세스 토큰 없음
                        
            2001: 액세스 토큰 만료됨
                        
            2002: 액세스 토큰이 유효하지 않음
                        
            2003: 리프레시 토큰이 유효하지 않음
                        
            2004: 리프레시 토큰이 유저의 리프레시 토큰과 일치하지 않음""",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
})
public @interface ApiDocumentResponse {

}
