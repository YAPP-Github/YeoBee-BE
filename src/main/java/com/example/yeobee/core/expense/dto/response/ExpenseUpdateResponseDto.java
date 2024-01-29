package com.example.yeobee.core.expense.dto.response;

import com.example.yeobee.core.expense.domain.Expense;
import com.example.yeobee.core.expense.domain.ExpenseMethod;
import com.example.yeobee.core.expense.domain.ExpenseType;
import com.example.yeobee.core.expense.dto.ExpensePhotoDto;
import com.example.yeobee.core.expense.dto.UserExpenseDto;
import java.math.BigDecimal;
import java.util.List;

public record ExpenseUpdateResponseDto(Long id, Long tripId, ExpenseType expenseType,
                                       BigDecimal amount,
                                       String currencyCode, ExpenseMethod expenseMethod, String name, Long payerId,
                                       List<UserExpenseDto> payerList, List<ExpensePhotoDto> imageList) {

    public ExpenseUpdateResponseDto(Expense expense) {
        this(expense.getId(),
             expense.getTrip().getId(),
             expense.getExpenseType(),
             expense.getAmount(),
             expense.getTripCurrency().getCurrency().getCode(),
             expense.getExpenseMethod(),
             expense.getName(),
             expense.getPayerId(),
             expense.getUserExpenseList().stream().map(
                 UserExpenseDto::new).toList(),
             expense.getExpensePhotoList().stream().map(ExpensePhotoDto::new).toList());
    }
}
