package com.example.yeobee.core.expense.domain;

import com.example.yeobee.core.expense.dto.common.UserExpenseFilter;
import com.example.yeobee.core.expense.dto.response.UserExpenseListRetrieveResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserExpenseRepositoryCustom {

    Page<UserExpenseListRetrieveResponseDto> findByFilter(UserExpenseFilter filter, Pageable pageable);
}
