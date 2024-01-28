package com.example.yeobee.core.expense.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.expense.domain.UserExpenseRepository;
import com.example.yeobee.core.expense.dto.common.UserExpenseFilter;
import com.example.yeobee.core.expense.dto.request.UserExpenseListRetrieveRequestDto;
import com.example.yeobee.core.expense.dto.response.UserExpenseListRetrieveResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserExpenseService {

    private final UserExpenseRepository userExpenseRepository;

    public Page<UserExpenseListRetrieveResponseDto> retrieveUserExpenseList(UserExpenseListRetrieveRequestDto request) {
        if (request.tripUserId() == null || request.pageIndex() == null || request.pageSize() == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        return userExpenseRepository.findByFilter(new UserExpenseFilter(request),
                                                  PageRequest.of(request.pageIndex(), request.pageSize()));
    }
}
