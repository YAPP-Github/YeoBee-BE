package com.example.yeobee.core.country.application;

import com.example.yeobee.core.country.domain.Country;
import com.example.yeobee.core.country.domain.CountryRepository;
import com.example.yeobee.core.country.dto.response.CountryListResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CountryService {

    private final CountryRepository countryRepository;

    @Value("${spring.cloud.aws.cdn.url}")
    private String cdnUrl;

    public CountryListResponseDto findAllCountriesWithMostTrips() {
        List<Country> countries = countryRepository.findAllCountriesWithMostTrips();
        return CountryListResponseDto.of(countries, cdnUrl);
    }
}
