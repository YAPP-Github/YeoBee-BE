package com.example.yeobee.core.trip.dto.request;

import java.time.LocalDate;
import java.util.List;

public record CreateTripRequestDto(
    String title,
    LocalDate startDate,
    LocalDate endDate,
    List<CreateTripCountryRequestDto> countryList,
    List<CreateTripTripUserRequestDto> tripUserList
) {

    public record CreateTripCountryRequestDto(String name) {

    }

    public record CreateTripTripUserRequestDto(String name, String profileImageUrl) {

    }
}