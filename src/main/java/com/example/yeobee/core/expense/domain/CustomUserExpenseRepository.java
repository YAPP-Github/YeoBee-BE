package com.example.yeobee.core.expense.domain;

import com.example.yeobee.core.calculation.domain.CalculationResult;
import com.example.yeobee.core.expense.dto.request.UserExpenseFilter;
import com.example.yeobee.core.expense.dto.response.UserExpenseListRetrieveResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserExpenseRepository {

    Page<UserExpenseListRetrieveResponseDto> findByFilter(UserExpenseFilter filter, Pageable pageable);

    List<CalculationResult> getCalculationResult(Long tripId, ExpenseType expenseType);
}
