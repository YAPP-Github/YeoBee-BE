package com.example.yeobee.core.tripCountry.domain;

import com.example.yeobee.core.country.domain.Country;
import com.example.yeobee.core.currencyUnit.domain.CurrencyUnit;
import com.example.yeobee.core.trip.domain.Trip;
import jakarta.persistence.*;

@Entity
public class TripCountry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ExchangeRateType exchangeRateType;

    private Double exchangeRateValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "currency_unit_id")
    private CurrencyUnit currencyUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "trip_id")
    private Trip trip;
}
