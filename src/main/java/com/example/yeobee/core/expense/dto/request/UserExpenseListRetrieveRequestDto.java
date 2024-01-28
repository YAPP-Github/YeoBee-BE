package com.example.yeobee.core.expense.dto.request;

import com.example.yeobee.core.expense.domain.ExpenseMethod;

public record UserExpenseListRetrieveRequestDto(Integer pageIndex, Integer pageSize, Long tripUserId,
                                                ExpenseMethod expenseMethod) {

}
