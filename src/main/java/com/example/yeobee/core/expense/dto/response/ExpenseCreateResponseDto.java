package com.example.yeobee.core.expense.dto.response;

import com.example.yeobee.core.expense.domain.ExpenseMethod;
import com.example.yeobee.core.expense.domain.ExpenseType;
import com.example.yeobee.core.expense.dto.common.ExpensePhotoDto;
import com.example.yeobee.core.expense.dto.common.UserExpenseDto;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public record ExpenseCreateResponseDto(Long id, Long tripId, ZonedDateTime payedAt, ExpenseType expenseType,
                                       BigDecimal amount,
                                       String currencyCode, ExpenseMethod expenseMethod, String name, Long payerId,
                                       List<UserExpenseDto> payerList, List<ExpensePhotoDto> imageList) {

}
