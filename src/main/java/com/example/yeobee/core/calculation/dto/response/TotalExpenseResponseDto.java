package com.example.yeobee.core.calculation.dto.response;

import com.example.yeobee.core.calculation.domain.CalculationResult;
import java.util.List;

public record TotalExpenseResponseDto(List<TotalExpense> totalExpenseList, Long totalAmount, Long leftSharedBudget) {

    public TotalExpenseResponseDto(List<CalculationResult> calculationResultList, Long leftSharedBudget) {
        this(calculationResultList.stream().map(TotalExpense::new).toList(),
             calculationResultList.stream()
                 .map(CalculationResult::getCalculationSum)
                 .mapToLong(Double::longValue)
                 .sum(),
             leftSharedBudget);
    }

    public record TotalExpense(Long tripUserId, Long userId, String name, String profileImageUrl, Long koreanAmount) {

        public TotalExpense(CalculationResult calculationResult) {
            this(calculationResult.getTripUser().getId(),
                 calculationResult.getTripUser().getUserId(),
                 calculationResult.getTripUser().getTripUserName(),
                 calculationResult.getTripUser().getProfileImageUrl(),
                 calculationResult.getCalculationSum().longValue());
        }
    }
}
