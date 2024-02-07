package com.example.yeobee.core.currency.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

    @Query("select c from Currency c join TripCurrency tc on c = tc.currency where tc.trip.id = :tripId")
    List<Currency> findAllByTripId(long tripId);
}
