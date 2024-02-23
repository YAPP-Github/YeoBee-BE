package com.example.yeobee.core.currency.presentation;

import com.example.yeobee.core.currency.application.CurrencyService;
import com.example.yeobee.core.currency.dto.request.UpdateCurrencyRateRequestDto;
import com.example.yeobee.core.currency.dto.response.CurrencyListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/currencies")
@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public CurrencyListResponseDto getCurrencies(@RequestParam(required = false) Long tripId) {
        if (tripId != null) {
            return currencyService.getTripCurrencies(tripId);
        }
        return currencyService.getAllCurrencies();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{currencyCode}/rate")
    public void updateCurrencyRate(
        @RequestBody UpdateCurrencyRateRequestDto request,
        @PathVariable String currencyCode
    ) {
        currencyService.updateCurrencyRate(request, currencyCode);
    }
}
