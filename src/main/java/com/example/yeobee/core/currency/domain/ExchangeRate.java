package com.example.yeobee.core.currency.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Embeddable
@Getter
public class ExchangeRate {

    @Enumerated(EnumType.STRING)
    private ExchangeRateType exchangeRateType;

    private Double exchangeRateValue;

    private Long exchangeRateStandard;
}
