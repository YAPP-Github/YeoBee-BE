package com.example.yeobee.core.country.presentation;

import com.example.yeobee.core.country.application.CountryService;
import com.example.yeobee.core.country.dto.response.CountryListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/countries")
@RestController
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public CountryListResponseDto getAllCountries() {
        return countryService.getAllCountries();
    }
}
