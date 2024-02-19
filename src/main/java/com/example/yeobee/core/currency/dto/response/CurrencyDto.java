package com.example.yeobee.core.currency.dto.response;

import java.math.BigDecimal;

public record CurrencyDto(String code, String name, BigDecimal val) {

}

