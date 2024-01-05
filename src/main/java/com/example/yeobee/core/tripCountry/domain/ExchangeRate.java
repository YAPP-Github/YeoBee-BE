package com.example.yeobee.core.tripCountry.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class ExchangeRate {

    @Enumerated(EnumType.STRING)
    private ExchangeRateType exchangeRateType;

    private Double exchangeRateValue;
}
