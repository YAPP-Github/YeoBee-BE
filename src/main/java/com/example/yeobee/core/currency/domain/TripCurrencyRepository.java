package com.example.yeobee.core.currency.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripCurrencyRepository extends JpaRepository<TripCurrency, Long> {

    public Optional<TripCurrency> findByTripIdAndCurrencyCode(Long tripId, String currencyCode);
}
