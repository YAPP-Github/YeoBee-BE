package com.example.yeobee.core.expense.dto.common;

import com.example.yeobee.core.expense.domain.ExpenseMethod;
import com.example.yeobee.core.expense.domain.ExpenseType;
import com.example.yeobee.core.expense.dto.request.ExpenseListRetrieveRequestDto;
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
