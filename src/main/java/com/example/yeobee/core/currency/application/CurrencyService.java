package com.example.yeobee.core.currency.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.currency.domain.Currency;
import com.example.yeobee.core.currency.domain.CurrencyRepository;
import com.example.yeobee.core.currency.dto.request.UpdateCurrencyRateRequestDto;
import com.example.yeobee.core.currency.dto.response.CurrencyListResponseDto;
import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.trip.domain.TripCurrency;
import com.example.yeobee.core.trip.domain.TripCurrencyRepository;
import com.example.yeobee.core.trip.domain.TripRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final TripCurrencyRepository tripCurrencyRepository;
    private final TripRepository tripRepository;

    public CurrencyListResponseDto getAllCurrencies() {
        List<Currency> currencies = currencyRepository.findAll();
        return CurrencyListResponseDto.ofCurrencies(currencies);
    }

    public CurrencyListResponseDto getTripCurrencies(Long tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
        List<TripCurrency> tripCurrencies = trip.getTripCurrencyList();
        return CurrencyListResponseDto.ofTripCurrencies(tripCurrencies);
    }

    @Transactional
    public void updateCurrencyRate(UpdateCurrencyRateRequestDto request, String currencyCode) {
        // find TripCurrency
        TripCurrency tripCurrency = tripCurrencyRepository.findByTripIdAndCurrencyCode(request.tripId(), currencyCode)
            .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_CURRENCY_NOT_FOUND));

        // update TripCurrency
        tripCurrency.getExchangeRate().update(request.value(), request.standard());
    }
}
