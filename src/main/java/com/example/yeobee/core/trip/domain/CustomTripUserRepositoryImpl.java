package com.example.yeobee.core.trip.domain;

import static com.example.yeobee.core.currency.domain.QTripCurrency.tripCurrency;
import static com.example.yeobee.core.expense.domain.QExpense.expense;
import static com.example.yeobee.core.expense.domain.QUserExpense.userExpense;
import static com.example.yeobee.core.trip.domain.QTripUser.tripUser;

import com.example.yeobee.core.expense.domain.ExpenseType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomTripUserRepositoryImpl implements CustomTripUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CalculationResult> getTotalExpensePerTripUser(Long tripId, ExpenseType expenseType) {
        return queryFactory
            .select(Projections.constructor(CalculationResult.class,
                                            tripUser,
                                            new CaseBuilder()
                                                .when(expense.expenseType.eq(expenseType))
                                                .then(userExpense.amount
                                                          .multiply(tripCurrency.exchangeRate.exchangeRateValue)
                                                          .divide(tripCurrency.exchangeRate.exchangeRateStandard))
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
