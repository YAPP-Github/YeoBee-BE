package com.example.yeobee.core.expense.dto.request;

import com.example.yeobee.core.expense.domain.ExpenseCategory;
import com.example.yeobee.core.expense.domain.ExpenseMethod;
import com.example.yeobee.core.expense.domain.ExpenseType;
import com.example.yeobee.core.expense.dto.common.ExpensePhotoDto;
import com.example.yeobee.core.expense.dto.common.UserExpenseDto;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public record ExpenseCreateRequestDto(Long tripId, ZonedDateTime payedAt, ExpenseType expenseType, BigDecimal amount,
                                      String currencyCode, ExpenseMethod expenseMethod, ExpenseCategory expenseCategory,
                                      String name, Long payerId,
                                      List<UserExpenseDto> payerList, List<ExpensePhotoDto> imageList) {


}
