package com.example.yeobee.core.currency.domain;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeRate {

    private BigDecimal value;

    private Long standard;

    public Long getKoreanAmount(BigDecimal amount) {
        return value.divide(BigDecimal.valueOf(standard), 2, RoundingMode.HALF_UP)
            .multiply(amount)
            .longValue();
    }

    public ExchangeRate(BigDecimal value, Long standard) {
        this.value = value;
        this.standard = standard;
    }

    public void update(BigDecimal value, long standard) {
        this.value = value;
        this.standard = standard;
        // TODO: validate
    }
}
