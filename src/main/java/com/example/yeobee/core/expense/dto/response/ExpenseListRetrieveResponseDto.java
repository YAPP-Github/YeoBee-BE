package com.example.yeobee.core.expense.dto.response;

import com.example.yeobee.core.expense.domain.Expense;
import com.example.yeobee.core.expense.domain.ExpenseCategory;
import java.math.BigDecimal;

public record ExpenseListRetrieveResponseDto(Long id, ExpenseCategory expenseCategoryImage, String name,
                                             BigDecimal amount, String currencyCode, Long koreanAmount) {

    public ExpenseListRetrieveResponseDto(Expense expense) {
        this(expense.getId(),
             expense.getExpenseCategory(),
             expense.getName(),
             expense.getAmount(),
             expense.getCurrencyCode(),
             expense.getKoreanAmount());
    }
}
