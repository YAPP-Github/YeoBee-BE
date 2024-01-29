package com.example.yeobee.core.expense.dto.request;

import com.example.yeobee.core.expense.domain.ExpenseMethod;

public record UserExpenseFilter(Long tripUserId, ExpenseMethod expenseMethod) {

    public UserExpenseFilter(UserExpenseListRetrieveRequestDto request) {
        this(request.tripUserId(), request.expenseMethod());
    }
}
