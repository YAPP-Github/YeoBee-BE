package com.example.yeobee.core.currency.application;

import com.example.yeobee.core.currency.domain.Currency;
import com.example.yeobee.core.currency.domain.CurrencyRepository;
import com.example.yeobee.core.currency.dto.request.UpdateCurrencyRateRequestDto;
import com.example.yeobee.core.currency.dto.response.CurrencyListResponseDto;
import com.example.yeobee.core.trip.domain.TripCurrency;
import com.example.yeobee.core.trip.domain.TripCurrencyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final TripCurrencyRepository tripCurrencyRepository;

    public CurrencyListResponseDto getAllCurrencies() {
        List<Currency> currencies = currencyRepository.findAll();
        return CurrencyListResponseDto.of(currencies);
    }

    public CurrencyListResponseDto getTripCurrencies(Long tripId) {
        List<Currency> currencies = currencyRepository.findAllByTripId(tripId);
        return CurrencyListResponseDto.of(currencies);
    }

    @Transactional
    public void updateCurrencyRate(UpdateCurrencyRateRequestDto request, long tripId, String currencyCode) {
        // find TripCurrency
        TripCurrency tripCurrency = tripCurrencyRepository.findByTripIdAndCurrencyCode(tripId, currencyCode)
            .orElseThrow(); // TODO: 예외 처리

        // update TripCurrency
        tripCurrency.getExchangeRate().update(request.value(), request.standard());
    }
}
