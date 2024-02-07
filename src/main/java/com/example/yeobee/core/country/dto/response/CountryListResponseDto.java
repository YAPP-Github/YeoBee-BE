package com.example.yeobee.core.country.dto.response;

import com.example.yeobee.core.country.domain.Continent;
import com.example.yeobee.core.country.domain.Country;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CountryListResponseDto(Map<String, List<CountryResponseDto>> countryList) {

    public static CountryListResponseDto of(List<Country> countries) {
        Map<String, List<CountryResponseDto>> map = new HashMap<>();
        for (Continent continent : Continent.values()) {
            map.put(continent.getName(), new ArrayList<>());
        }

        for (Country country : countries) {
            String continentName = country.getContinent().getName();
            CountryResponseDto countryResponseDto = new CountryResponseDto(
                country.getName(),
                country.getFlagImageUrl(),
                country.getCoverImageUrl(),
                continentName
            );
            map.get(continentName).add(countryResponseDto);
        }

        return new CountryListResponseDto(map);
    }

    public record CountryResponseDto(
        String name,
        String flagImageUrl,
        String coverImageUrl,
        String continent
    ) {

    }
}
