package com.example.yeobee.core.currency.dto.response;

import com.example.yeobee.core.currency.domain.Currency;
import com.example.yeobee.core.currency.domain.ExchangeRate;
import com.example.yeobee.core.trip.domain.TripCurrency;
import java.math.BigDecimal;
import java.util.List;

public record CurrencyListResponseDto(
    List<CurrencyResponseDto> currencyList
) {

    public static CurrencyListResponseDto ofCurrencies(List<Currency> currencies) {
        return new CurrencyListResponseDto(
            currencies.stream().map(CurrencyResponseDto::of).toList()
        );
    }

    public static CurrencyListResponseDto ofTripCurrencies(List<TripCurrency> tripCurrencies) {
        return new CurrencyListResponseDto(
            tripCurrencies.stream().map(CurrencyResponseDto::of).toList()
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

        public static CurrencyResponseDto of(TripCurrency tripCurrency) {
            return new CurrencyResponseDto(
                tripCurrency.getCurrency().getName(),
                tripCurrency.getCurrency().getCode(),
                ExchangeRateResponseDto.of(tripCurrency.getExchangeRate())
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
