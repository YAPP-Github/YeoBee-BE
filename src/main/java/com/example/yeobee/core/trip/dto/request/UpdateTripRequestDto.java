package com.example.yeobee.core.trip.dto.request;

import java.time.LocalDate;
import java.util.List;

public record UpdateTripRequestDto(
    String title,
    LocalDate startDate,
    LocalDate endDate,
    List<UpdateTripTripUserRequestDto> tripUserList
) {

    public record UpdateTripTripUserRequestDto(long id, String name) {

    }
}