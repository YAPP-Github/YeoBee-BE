package com.example.yeobee.core.expense.dto.response;

import com.example.yeobee.core.expense.domain.UserExpense;
import java.math.BigDecimal;

public record UserExpenseDetailDto(Long id, Long userId, Long tripUserId, String tripUserName, String profileImageUrl,
                                   BigDecimal amount) {

    public UserExpenseDetailDto(UserExpense userExpense) {
        this(userExpense.getId(),
             userExpense.getTripUser().getUserId(),
             userExpense.getTripUser().getId(),
             userExpense.getTripUser().getName(),
             userExpense.getTripUser().getProfileImageUrl(),
             userExpense.getAmount());
    }
}
