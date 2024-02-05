package com.example.yeobee.core.trip.dto.response;

import com.example.yeobee.core.trip.domain.TripUser;

public record CalculationUser(Long userId, String name, String profileImageUrl) {

    public CalculationUser(TripUser tripUser) {
        this(tripUser.getUserId(), tripUser.getTripUserName(), tripUser.getProfileImageUrl());
    }
}
