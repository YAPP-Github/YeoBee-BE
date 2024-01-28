package com.example.yeobee.core.expense.dto.request;

import com.example.yeobee.core.expense.domain.ExpenseMethod;
import com.example.yeobee.core.expense.domain.ExpenseType;
import java.time.LocalDate;

public record ExpenseListRetrieveRequestDto(Long tripId, Integer pageIndex, Integer pageSize, ExpenseType expenseType,
                                            LocalDate payedAt, ExpenseMethod expenseMethod, String currencyCode) {

}
