package com.example.yeobee.core.expense.domain;

import com.example.yeobee.core.expense.dto.request.ExpenseListFilter;
import com.example.yeobee.core.expense.dto.response.ExpenseListRetrieveResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomExpenseRepository {

    Page<ExpenseListRetrieveResponseDto> findByFilter(ExpenseListFilter filter, Pageable pageable);
}
