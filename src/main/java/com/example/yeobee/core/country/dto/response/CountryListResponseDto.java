package com.example.yeobee.core.country.dto.response;

import com.example.yeobee.core.country.domain.Country;
import java.util.List;

public record CountryListResponseDto(List<CountryResponseDto> countryList) {

    public static CountryListResponseDto of(List<Country> countries) {
        return new CountryListResponseDto(
            countries.stream().map(country -> new CountryResponseDto(
                country.getName(),
                country.getFlagImageUrl(),
                country.getCoverImageUrl(),
                country.getContinent().getName()
            )).toList()
        );
    }

    public record CountryResponseDto(
        String name,
        String flagImageUrl,
        String coverImageUrl,
        String continent
    ) {

    }
}
