package com.example.yeobee.core.currency.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Currency {

    @Id
    private String code;

    private String name;

    @Embedded
    private ExchangeRate exchangeRate;

    public Currency(String code) {
        this.code = code;
    }

    public Currency(String code, String name, BigDecimal exchangeRateValue) {
        this.code = code;
        this.name = name;
        this.exchangeRate = new ExchangeRate(exchangeRateValue, 1L);
    }

    public Currency(String name, ExchangeRate exchangeRate) {
        this.name = name;
        this.exchangeRate = exchangeRate;
    }
}
