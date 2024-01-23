package com.example.yeobee.core.expense.dto.common;

import com.example.yeobee.core.expense.domain.UserExpense;
import java.math.BigDecimal;

public record UserExpenseDetailDto(Long id, Long tripUserId, String tripUserName, BigDecimal amount) {

    public UserExpenseDetailDto(UserExpense userExpense) {
        this(userExpense.getId(),
             userExpense.getTripUser().getId(),
             userExpense.getTripUser().getName(),
             userExpense.getAmount());
    }
}
