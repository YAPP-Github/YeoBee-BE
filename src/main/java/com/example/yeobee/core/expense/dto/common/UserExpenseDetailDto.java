package com.example.yeobee.core.expense.dto.common;

import com.example.yeobee.core.expense.domain.UserExpense;
import java.math.BigDecimal;

public record UserExpenseDetailDto(Long id, Long tripUserId, String tripUserName, String profileImageUrl,
                                   BigDecimal amount) {

    public UserExpenseDetailDto(UserExpense userExpense, Long userId) {
        this(userExpense.getId(),
             userExpense.getTripUser().getId(),
             userExpense.getTripUser().getTripUserName(userId),
             userExpense.getTripUser().getProfileImageUrl(),
             userExpense.getAmount());
    }
}
