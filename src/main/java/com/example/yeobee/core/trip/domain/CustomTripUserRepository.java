package com.example.yeobee.core.trip.domain;

import com.example.yeobee.core.expense.domain.ExpenseType;
import java.util.List;

public interface CustomTripUserRepository {

    List<CalculationResult> getTotalExpensePerTripUser(Long tripId, ExpenseType expenseType);
}
