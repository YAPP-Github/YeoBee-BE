package com.example.yeobee.core.expense.dto;

import com.example.yeobee.core.expense.domain.UserExpense;
import java.math.BigDecimal;

public record UserExpenseDto(Long id, Long tripUserId, BigDecimal amount) {

    public UserExpenseDto(UserExpense userExpense) {
        this(userExpense.getId(), userExpense.getTripUser().getId(), userExpense.getAmount());
    }
}
