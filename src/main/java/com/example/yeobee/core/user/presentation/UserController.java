package com.example.yeobee.core.user.presentation;

import com.example.yeobee.core.auth.annotation.AuthUser;
import com.example.yeobee.core.user.application.UserService;
import com.example.yeobee.core.user.domain.User;
import com.example.yeobee.core.user.dto.request.UserUpdateRequestDto;
import com.example.yeobee.core.user.dto.response.UserInfoResponseDto;
import com.example.yeobee.core.user.dto.response.UserUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/me")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@AuthUser User user) {
        return ResponseEntity.ok(userService.getUserInfo(user));
    }

    @PatchMapping(value = "/me")
    public ResponseEntity<UserUpdateResponseDto> updateUserInfo(
        @AuthUser User user,
        @RequestBody UserUpdateRequestDto request) {
        return ResponseEntity.ok(userService.updateUserInfo(user, request));
    }
}
