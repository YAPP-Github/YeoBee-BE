package com.example.yeobee.core.currency.dto.request;

import java.math.BigDecimal;

public record UpdateCurrencyRateRequestDto(Long standard, BigDecimal value, Long tripId) {

}
