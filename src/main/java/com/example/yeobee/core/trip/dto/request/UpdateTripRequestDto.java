package com.example.yeobee.core.trip.dto.request;

import java.time.LocalDate;
import java.util.List;

public record UpdateTripRequestDto(
    String title,
    LocalDate startDate,
    LocalDate endDate,
    List<TripUserRequestDto> tripUserList
) {

    public record CountryRequestDto(String name) {

    }

    public record TripUserRequestDto(long id, String name) {

    }
}