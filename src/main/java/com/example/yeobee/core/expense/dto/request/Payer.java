package com.example.yeobee.core.expense.dto.request;

import com.example.yeobee.core.expense.domain.UserExpense;
import java.math.BigDecimal;

public record Payer(Long id, Long tripUserId, BigDecimal amount) {

    public Payer(UserExpense userExpense) {
        this(userExpense.getId(), userExpense.getTripUser().getId(), userExpense.getAmount());
    }
}
