package com.example.yeobee.core.trip.dto.response;

import com.example.yeobee.core.trip.domain.TripUser;

public record Calculation(CalculationUser sender, CalculationUser receiver, Long koreanAmount) {

    public Calculation(TripUser sender, TripUser receiver, Long koreanAmount) {
        this(new CalculationUser(sender), new CalculationUser(receiver), koreanAmount);
    }

    public record CalculationUser(Long userId, String name, String profileImageUrl) {

        public CalculationUser(TripUser tripUser) {
            this(tripUser.getUserId(), tripUser.getTripUserName(), tripUser.getProfileImageUrl());
        }
    }
}
