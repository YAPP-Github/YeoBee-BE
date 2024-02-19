package com.example.yeobee.core.currency.domain;

import com.example.yeobee.core.currency.dto.response.CurrencyDto;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    public Currency(CurrencyDto currencyDto) {
        this.code = currencyDto.code();
        this.name = currencyDto.name();
        this.exchangeRate = new ExchangeRate(currencyDto.val(), 1L);
    }


    public Currency(String name, ExchangeRate exchangeRate) {
        this.name = name;
        this.exchangeRate = exchangeRate;
    }
}
