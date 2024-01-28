package com.example.yeobee.core.trip.domain;

import com.example.yeobee.core.currency.domain.Currency;
import com.example.yeobee.core.currency.domain.ExchangeRate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TripCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "currency_code")
    private Currency currency;

    @Embedded
    private ExchangeRate exchangeRate;

    public TripCurrency(Trip trip, Currency currency) {
        this.trip = trip;
        this.currency = currency;
        this.exchangeRate = currency.getExchangeRate();
    }
}
