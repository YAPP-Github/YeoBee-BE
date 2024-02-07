package com.example.yeobee.core.currency.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryCurrencyRepository extends JpaRepository<CountryCurrency, Long> {

    Optional<CountryCurrency> findByCountryNameAndCurrencyCode(String countryName, String currencyCode);
}
