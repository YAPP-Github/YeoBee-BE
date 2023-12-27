package com.example.yeobee.auth.kakao.controller;

import com.example.yeobee.auth.authToken.dto.response.AuthResponseDto;
import com.example.yeobee.auth.kakao.dto.KakaoLoginRequestDto;
import com.example.yeobee.auth.kakao.service.KakaoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Kakao v1 API", description = "Kakao Oauth 관련 api")
@RequestMapping("/v1/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 통해 받은 토큰을 이용해 유저를 생성 혹은 식별 후 access token 맟 refresh 토큰 발급")
    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponseDto> kakaoLogin(@RequestBody KakaoLoginRequestDto kakaoLoginRequest) {
        return ResponseEntity.ok(kakaoAuthService.login(kakaoLoginRequest));
    }
}
