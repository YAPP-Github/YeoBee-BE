package com.example.yeobee.core.expense.dto.response;

import com.example.yeobee.core.expense.domain.Expense;
import com.example.yeobee.core.expense.domain.ExpenseMethod;
import com.example.yeobee.core.expense.dto.ExpensePhotoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

public record ExpenseDetailRetrieveResponseDto(BigDecimal amount, String currencyCode, Long koreanAmount,
                                               ExpenseMethod expenseMethod, String expenseCategoryName,
                                               String name, @Schema(nullable = true) Long payerUserId, String payerName,
                                               List<UserExpenseDetailDto> payerList, List<ExpensePhotoDto> imageList) {

    public ExpenseDetailRetrieveResponseDto(Expense expense) {
        this(expense.getAmount(),
             expense.getTripCurrency().getCurrency().getCode(),
             expense.getKoreanAmount(),
             expense.getExpenseMethod(),
             expense.getExpenseCategory().getName(),
             expense.getName(),
             expense.getPayer().getUserId(),
             expense.getPayerName(),
             expense.getUserExpenseList()
                 .stream()
                 .map(UserExpenseDetailDto::new)
                 .toList(),
             expense.getExpensePhotoList()
                 .stream()
                 .map(ExpensePhotoDto::new)
                 .toList());
    }
}
