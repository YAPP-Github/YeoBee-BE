package com.example.yeobee.core.tripCountry.domain;

import com.example.yeobee.core.country.domain.Country;
import com.example.yeobee.core.currencyUnit.domain.CurrencyUnit;
import com.example.yeobee.core.trip.domain.Trip;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class TripCountry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ExchangeRate exchangeRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_unit_id")
    private CurrencyUnit currencyUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
