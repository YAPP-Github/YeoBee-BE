package com.example.yeobee.core.trip.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripCurrencyRepository extends JpaRepository<TripCurrency, Long> {

    Optional<TripCurrency> findByTripIdAndCurrencyCode(Long tripId, String currencyCode);

    List<TripCurrency> findAllByTripId(Long tripId);
}
