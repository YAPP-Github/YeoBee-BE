package com.example.yeobee.core.expense.dto.request;

import com.example.yeobee.core.expense.domain.ExpenseMethod;
import com.example.yeobee.core.expense.domain.ExpenseType;
import com.example.yeobee.core.trip.domain.Trip;
import java.time.LocalDate;

public record ExpenseListFilter(Trip trip, ExpenseType expenseType, LocalDate payedAt,
                                ExpenseMethod expenseMethod, String currencyCode) {

}
