package com.example.yeobee.core.calculation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record BudgetResponseDto(Budget sharedBudget, Budget individualBudget) {

    public record Budget(@Schema(nullable = true) Long leftBudget, Long budgetIncome, Long budgetExpense) {

    }
}
