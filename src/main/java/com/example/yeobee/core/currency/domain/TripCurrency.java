package com.example.yeobee.core.currency.domain;

import com.example.yeobee.core.trip.domain.Trip;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class TripCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ExchangeRate exchangeRate;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "currency_code")
    private Currency currency;
}
