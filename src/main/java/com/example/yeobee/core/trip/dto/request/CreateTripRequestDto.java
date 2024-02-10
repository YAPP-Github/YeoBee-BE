package com.example.yeobee.core.trip.dto.request;

import com.example.yeobee.core.trip.domain.TripUserDefaultProfileImageType;
import java.time.LocalDate;
import java.util.List;

public record CreateTripRequestDto(
    String title,
    LocalDate startDate,
    LocalDate endDate,
    List<CountryRequestDto> countryList,
    List<TripUserRequestDto> tripUserList
) {

    public record CountryRequestDto(String name) {

    }

    public record TripUserRequestDto(String name, TripUserDefaultProfileImageType profileImageType) {

    }
}