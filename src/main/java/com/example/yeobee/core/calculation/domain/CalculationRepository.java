package com.example.yeobee.core.calculation.domain;

import com.example.yeobee.core.expense.domain.ExpenseType;
import com.example.yeobee.core.trip.domain.TripUser;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationRepository {

    List<CalculationResult> getCalculationResult(Long tripId);

    Long getTotalBudgetIncome(Long tripId, ExpenseType expenseType);

    Long getTotalBudgetExpense(Long tripId, ExpenseType expenseType, TripUser tripUser);

    List<CalculationResult> getTotalExpensePerTripUser(Long tripId, ExpenseType expenseType);

    List<CalculationResult> getCalculationResult(Long tripId, ExpenseType expenseType);

    Long getTotalExpense(Long tripId, ExpenseType expenseType);
}
