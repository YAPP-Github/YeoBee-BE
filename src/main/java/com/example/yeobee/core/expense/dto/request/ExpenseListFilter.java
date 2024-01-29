package com.example.yeobee.core.expense.dto.request;

import com.example.yeobee.core.expense.domain.ExpenseMethod;
import com.example.yeobee.core.expense.domain.ExpenseType;
import java.time.LocalDate;

public record ExpenseListFilter(Long tripId, ExpenseType expenseType, LocalDate payedAt,
                                ExpenseMethod expenseMethod, String currencyCode) {

    public ExpenseListFilter(ExpenseListRetrieveRequestDto request) {
        this(request.tripId(),
             request.expenseType(),
             request.payedAt(),
             request.expenseMethod(),
             request.currencyCode());
    }
}
