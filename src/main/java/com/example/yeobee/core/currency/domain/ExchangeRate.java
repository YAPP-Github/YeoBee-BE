package com.example.yeobee.core.currency.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Getter;

@Embeddable
@Getter
public class ExchangeRate {

    @Enumerated(EnumType.STRING)
    private ExchangeRateType exchangeRateType;

    private BigDecimal exchangeRateValue;

    private Long exchangeRateStandard;

    public Long getKoreanAmount(BigDecimal amount) {
        return exchangeRateValue.divide(BigDecimal.valueOf(exchangeRateStandard), 2, RoundingMode.HALF_UP)
            .multiply(amount)
            .longValue();
    }
}
