package com.example.yeobee.auth.apple.controller;

import com.example.yeobee.auth.apple.dto.request.AppleLoginRequestDto;
import com.example.yeobee.auth.apple.service.AppleAuthService;
import com.example.yeobee.auth.authToken.dto.response.AuthResponseDto;
import com.example.yeobee.common.annotation.ApiDocumentResponse;
import com.example.yeobee.common.annotation.ReqUser;
import com.example.yeobee.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Apple v1.1 API", description = "Apple Oauth 관련 api")
@Slf4j
@RestController
@RequestMapping("/api/v1.1/auth/apple")
@RequiredArgsConstructor
public class AppleAuthController {

    private final AppleAuthService appleAuthService;

    @Operation(summary = "애플 로그인", description = "애플 로그인을 통해 받은 정보를 이용해 유저를 생성 혹은 식별 후 access token 맟 refresh 토큰 발급")
    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<AuthResponseDto> appleLogin(@RequestBody AppleLoginRequestDto appleLoginRequest) throws org.json.simple.parser.ParseException, IOException {
        AuthResponseDto authResponseDto = appleAuthService.login(appleLoginRequest);
        return ResponseEntity.ok(authResponseDto);
    }

    @ApiDocumentResponse
    @Operation(summary = "애플 회원탈퇴", description = "현재 로그인한 유저를 회원탈퇴 처리한다.")
    @DeleteMapping(value = "/unlink")
    public ResponseEntity<Void> appleUnlinkUser(@ReqUser User user) throws IOException {
        appleAuthService.unlinkUser(user);
        return ResponseEntity.noContent().build();
    }
}
