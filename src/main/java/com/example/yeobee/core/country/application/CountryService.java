package com.example.yeobee.core.country.application;

import com.example.yeobee.core.country.domain.Country;
import com.example.yeobee.core.country.domain.CountryRepository;
import com.example.yeobee.core.country.dto.response.CountryListResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryListResponseDto getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return CountryListResponseDto.of(countries);
    }
}
