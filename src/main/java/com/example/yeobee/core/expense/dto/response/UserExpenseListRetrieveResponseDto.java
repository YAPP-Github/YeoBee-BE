package com.example.yeobee.core.expense.dto.response;

import com.example.yeobee.core.expense.domain.ExpenseCategory;
import com.example.yeobee.core.expense.domain.UserExpense;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserExpenseListRetrieveResponseDto(ExpenseCategory expenseCategoryImage, String name,
                                                 LocalDateTime payedAt, BigDecimal amount, String currencyCode,
                                                 Long koreanAmount) {

    public UserExpenseListRetrieveResponseDto(UserExpense userExpense) {
        this(userExpense.getExpenseCategory(),
             userExpense.getExpenseName(),
             userExpense.getExpense().getPayedAt(),
             userExpense.getAmount(),
             userExpense.getCurrencyCode(),
             userExpense.getKoreanAmount());
    }
}
