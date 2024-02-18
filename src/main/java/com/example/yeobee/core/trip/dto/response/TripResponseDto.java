package com.example.yeobee.core.trip.dto.response;

import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.trip.domain.TripCountry;
import com.example.yeobee.core.trip.domain.TripUser;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public record TripResponseDto(
    long id,
    String title,
    LocalDate startDate,
    LocalDate endDate,
    List<CountryResponseDto> countryList,
    List<TripUserResponseDto> tripUserList,
    ZonedDateTime createdAt
) {

    public static TripResponseDto of(Trip trip, String cdnUrl) {
        return new TripResponseDto(
            trip.getId(),
            trip.getTitle(),
            trip.getPeriod().getStartDate(),
            trip.getPeriod().getEndDate(),
            trip.getTripCountryList().stream().map((e) -> CountryResponseDto.of(e, cdnUrl)).toList(),
            trip.getTripUserList().stream().map(TripUserResponseDto::of).toList(),
            trip.getCreatedAt()
        );
    }

    public record CountryResponseDto(
        String name,
        String flagImageUrl,
        String coverImageUrl
    ) {

        public static CountryResponseDto of(TripCountry tripCountry, String cdnUrl) {
            return new CountryResponseDto(
                tripCountry.getCountry().getName(),
                tripCountry.getCountry().getFlagImageUrl(),
                tripCountry.getCountry().getCoverImageUrlOrRandomImageUrl(cdnUrl)
            );
        }
    }

    public record TripUserResponseDto(
        long id,
        Long userId,
        String name,
        String profileImageUrl
    ) {

        public static TripUserResponseDto of(TripUser tripUser) {
            return new TripUserResponseDto(
                tripUser.getId(),
                tripUser.getUserId(),
                tripUser.getName(),
                tripUser.getProfileImageUrl()
            );
        }
    }
}
