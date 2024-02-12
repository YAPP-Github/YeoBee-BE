package com.example.yeobee.core.calculation.dto.response;

import com.example.yeobee.core.trip.domain.TripUser;
import io.swagger.v3.oas.annotations.media.Schema;

public record Calculation(CalculationUser sender, CalculationUser receiver, Long koreanAmount) {

    public Calculation(TripUser sender, TripUser receiver, Long koreanAmount) {
        this(new CalculationUser(sender), new CalculationUser(receiver), koreanAmount);
    }

    public record CalculationUser(@Schema(nullable = true) Long userId, String name, String profileImageUrl) {

        public CalculationUser(TripUser tripUser) {
            this(tripUser.getUserId(), tripUser.getName(), tripUser.getProfileImageUrl());
        }
    }
}
