package com.example.yeobee.core.calculation.domain;

import com.example.yeobee.core.expense.domain.ExpenseType;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationRepository {

    List<CalculationResult> getCalculationResult(Long tripId);

    Long getTotalSharedBudgetIncome();

    Long getTotalSharedBudgetExpense();

    List<CalculationResult> getTotalExpensePerTripUser(Long tripId, ExpenseType expenseType);

    List<CalculationResult> getCalculationResult(Long tripId, ExpenseType expenseType);
}
