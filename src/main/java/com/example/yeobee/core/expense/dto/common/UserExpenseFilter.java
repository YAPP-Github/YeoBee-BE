package com.example.yeobee.core.expense.dto.common;

import com.example.yeobee.core.expense.domain.ExpenseMethod;
import com.example.yeobee.core.expense.dto.request.UserExpenseListRetrieveRequestDto;

public record UserExpenseFilter(Long tripUserId, ExpenseMethod expenseMethod) {

    public UserExpenseFilter(UserExpenseListRetrieveRequestDto request) {
        this(request.tripUserId(), request.expenseMethod());
    }
}
