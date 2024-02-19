package com.example.yeobee.core.user.application;

import com.example.yeobee.core.trip.domain.TripUserRepository;
import com.example.yeobee.core.user.domain.User;
import com.example.yeobee.core.user.dto.request.UserStateUpdateRequestDto;
import com.example.yeobee.core.user.dto.request.UserUpdateRequestDto;
import com.example.yeobee.core.user.dto.response.UserInfoResponseDto;
import com.example.yeobee.core.user.dto.response.UserStateUpdateResponseDto;
import com.example.yeobee.core.user.dto.response.UserUpdateResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TripUserRepository tripUserRepository;

    public UserInfoResponseDto getUserInfo(User user) {
        Integer tripCount = tripUserRepository.getTripCountByUserId(user.getId());
        return new UserInfoResponseDto(user, tripCount);
    }

    @Transactional
    public UserUpdateResponseDto updateUserInfo(User user, UserUpdateRequestDto request) {
        user.updateInfo(request.nickname(), request.profileImageUrl());
        return new UserUpdateResponseDto(user);
    }

    @Transactional
    public UserStateUpdateResponseDto updateUserState(User user, UserStateUpdateRequestDto request) {
        user.updateUserState(request.userState());
        return new UserStateUpdateResponseDto(user.getState());
    }
}
