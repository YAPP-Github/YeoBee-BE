package com.example.yeobee.core.calculation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record BudgetResponseDto(SharedBudget sharedBudget, IndividualBudget individualBudget) {

    public record SharedBudget(@Schema(nullable = true) Long leftBudget, Long budgetIncome, Long budgetExpense,
                               Long totalExpense) {

    }

    public record IndividualBudget(@Schema(nullable = true) Long leftBudget, Long budgetIncome, Long budgetExpense) {

    }
}
