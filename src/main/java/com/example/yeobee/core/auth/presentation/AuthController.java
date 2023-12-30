package com.example.yeobee.core.auth.presentation;

import com.example.yeobee.core.auth.annotation.AuthUser;
import com.example.yeobee.core.auth.application.AppleAuthService;
import com.example.yeobee.core.auth.application.AuthService;
import com.example.yeobee.core.auth.application.KakaoAuthService;
import com.example.yeobee.core.auth.dto.request.AppleLoginRequestDto;
import com.example.yeobee.core.auth.dto.request.KakaoLoginRequestDto;
import com.example.yeobee.core.auth.dto.response.TokenResponseDto;
import com.example.yeobee.core.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppleAuthService appleAuthService;
    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping(value = "/login/apple")
    public ResponseEntity<TokenResponseDto> appleLogin(@RequestBody AppleLoginRequestDto appleLoginRequest) {
        TokenResponseDto tokenResponseDto = appleAuthService.login(appleLoginRequest);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping(value = "/login/kakao")
    public ResponseEntity<TokenResponseDto> kakaoLogin(@RequestBody KakaoLoginRequestDto kakaoLoginRequest) {
        return ResponseEntity.ok(kakaoAuthService.login(kakaoLoginRequest));
    }

    @DeleteMapping(value = "/logout")
    public ResponseEntity<Void> logout(@AuthUser User user){
        authService.logout(user);
        return ResponseEntity.noContent().build();
    }
}
