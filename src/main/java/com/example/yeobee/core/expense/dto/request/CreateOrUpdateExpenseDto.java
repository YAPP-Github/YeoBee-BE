package com.example.yeobee.core.expense.dto.request;

import com.example.yeobee.core.expense.dto.UserExpenseDto;
import java.util.List;

public record CreateOrUpdateExpenseDto(Long tripId, String currencyCode,
                                       List<UserExpenseDto> payerList, Long payerId) {

    public CreateOrUpdateExpenseDto(ExpenseCreateRequestDto request) {
        this(request.tripId(), request.currencyCode(), request.payerList(), request.payerId());
    }

    public CreateOrUpdateExpenseDto(ExpenseUpdateRequestDto request) {
        this(request.tripId(), request.currencyCode(), request.payerList(), request.payerId());
    }
}
