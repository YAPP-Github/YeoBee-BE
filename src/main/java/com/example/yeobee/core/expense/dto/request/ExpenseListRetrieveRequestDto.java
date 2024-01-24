package com.example.yeobee.core.expense.dto.request;

import com.example.yeobee.core.expense.domain.ExpenseMethod;
import com.example.yeobee.core.expense.domain.ExpenseType;
import java.time.ZonedDateTime;

public record ExpenseListRetrieveRequestDto(Long tripId, Long pageIndex, Long pageSize, ExpenseType expenseType,
                                            ZonedDateTime payedAt, ExpenseMethod expenseMethod, String currencyCode) {

}
