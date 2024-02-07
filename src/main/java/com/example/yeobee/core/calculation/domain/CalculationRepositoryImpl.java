package com.example.yeobee.core.calculation.domain;

import static com.example.yeobee.core.expense.domain.QExpense.expense;
import static com.example.yeobee.core.expense.domain.QUserExpense.userExpense;
import static com.example.yeobee.core.trip.domain.QTripCurrency.tripCurrency;
import static com.example.yeobee.core.trip.domain.QTripUser.tripUser;

import com.example.yeobee.core.expense.domain.ExpenseType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CalculationRepositoryImpl implements CalculationRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CalculationResult> getCalculationResult(Long tripId) {
        return queryFactory.select(Projections.constructor(CalculationResult.class,
                                                           expense.payer,
                                                           expense.amount
                                                               .multiply(tripCurrency.exchangeRate.value)
                                                               .divide(tripCurrency.exchangeRate.standard)
                                                               .sum().coalesce(BigDecimal.ZERO)))
            .from(expense)
            .leftJoin(expense.payer, tripUser)
            .leftJoin(expense.tripCurrency, tripCurrency)
            .where(expense.trip.id.eq(tripId)
                       .and(expense.expenseType.eq(ExpenseType.SHARED))
                       .and(expense.payer.isNotNull()))
            .groupBy(expense.payer.id)
            .fetch();
    }

    @Override
    public Long getTotalSharedBudgetIncome() {
        return queryFactory.select(expense.amount
                                       .multiply(tripCurrency.exchangeRate.value)
                                       .divide(tripCurrency.exchangeRate.standard)
                                       .sum().coalesce(BigDecimal.ZERO).longValue())
            .from(expense)
            .leftJoin(expense.tripCurrency, tripCurrency)
            .where(expense.expenseType.eq(ExpenseType.SHARED_BUDGET_INCOME))
            .fetchOne();
    }

    @Override
    public Long getTotalSharedBudgetExpense() {
        return queryFactory.select(expense.amount
                                       .multiply(tripCurrency.exchangeRate.value)
                                       .divide(tripCurrency.exchangeRate.standard)
                                       .sum().coalesce(BigDecimal.ZERO).longValue())
            .from(expense)
            .leftJoin(expense.tripCurrency, tripCurrency)
            .where(expense.expenseType.eq(ExpenseType.SHARED).and(expense.payer.isNull()))
            .fetchOne();
    }

    @Override
    public List<CalculationResult> getTotalExpensePerTripUser(Long tripId, ExpenseType expenseType) {
        return queryFactory
            .select(Projections.constructor(CalculationResult.class,
                                            tripUser,
                                            new CaseBuilder()
                                                .when(expense.expenseType.eq(expenseType))
                                                .then(userExpense.amount
                                                          .multiply(tripCurrency.exchangeRate.value)
                                                          .divide(tripCurrency.exchangeRate.standard))
                                                .otherwise(BigDecimal.ZERO)
                                                .sum().coalesce(BigDecimal.ZERO)))
            .from(tripUser)
            .leftJoin(userExpense).on(tripUser.id.eq(userExpense.tripUser.id))
            .leftJoin(userExpense.expense, expense)
            .leftJoin(expense.tripCurrency, tripCurrency)
            .where(tripUser.trip.id.eq(tripId))
            .groupBy(tripUser.id)
            .fetch();
    }
}
