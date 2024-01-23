package com.example.yeobee.core.expense.dto.common;

import com.example.yeobee.core.expense.domain.UserExpense;
import java.math.BigDecimal;

public record UserExpenseDetailDto(Long id, Long tripUserId, String tripUserName, BigDecimal amount) {

    public UserExpenseDetailDto(UserExpense userExpense, Long userId) {
        this(userExpense.getId(),
             userExpense.getTripUser().getId(),
             !userId.equals(userExpense.getTripUser().getId()) ? userExpense.getTripUser().getName()
                 : userExpense.getTripUser().getName() + " (나)",
             userExpense.getAmount());
    }
}
