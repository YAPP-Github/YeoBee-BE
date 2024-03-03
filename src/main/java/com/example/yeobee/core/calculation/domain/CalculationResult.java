package com.example.yeobee.core.calculation.domain;

import com.example.yeobee.core.trip.domain.TripUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class CalculationResult {

    private TripUser tripUser;
    @Setter
    private Double calculationSum;

    public void setValueMinus() {
        calculationSum *= -1;
    }

    public void addCalculationSum(double amount) {
        calculationSum += amount;
    }
}
