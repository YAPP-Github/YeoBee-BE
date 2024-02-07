package com.example.yeobee.core.currency.dto.response;

import com.example.yeobee.core.currency.domain.Currency;
import com.example.yeobee.core.currency.domain.ExchangeRate;
import java.math.BigDecimal;
import java.util.List;

public record CurrencyListResponseDto(
    List<CurrencyResponseDto> currencyList
) {

    public static CurrencyListResponseDto of(List<Currency> currencies) {
        return new CurrencyListResponseDto(
            currencies.stream().map(CurrencyResponseDto::of).toList()
        );
    }

    public record CurrencyResponseDto(
        String name,
        String code,
        ExchangeRateResponseDto exchangeRate
    ) {

        public static CurrencyResponseDto of(Currency currency) {
            return new CurrencyResponseDto(
                currency.getName(),
                currency.getCode(),
                ExchangeRateResponseDto.of(currency.getExchangeRate())
            );
        }
    }

    public record ExchangeRateResponseDto(
        BigDecimal value,
        Long standard
    ) {

        public static ExchangeRateResponseDto of(ExchangeRate exchangeRate) {
            return new ExchangeRateResponseDto(
                exchangeRate.getValue(),
                exchangeRate.getStandard()
            );
        }
    }
}
