package com.example.yeobee.core.expense.domain;

import static com.example.yeobee.core.currency.domain.QTripCurrency.tripCurrency;
import static com.example.yeobee.core.expense.domain.QExpense.expense;
import static com.example.yeobee.core.expense.domain.QUserExpense.userExpense;
import static com.example.yeobee.core.trip.domain.QTripUser.tripUser;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.expense.dto.request.UserExpenseFilter;
import com.example.yeobee.core.expense.dto.response.UserExpenseListRetrieveResponseDto;
import com.example.yeobee.core.trip.domain.CalculationResult;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CustomUserExpenseRepositoryImpl implements CustomUserExpenseRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserExpenseListRetrieveResponseDto> findByFilter(UserExpenseFilter filter, Pageable pageable) {
        List<UserExpense> userExpenseList = queryFactory
            .selectFrom(userExpense)
            .leftJoin(userExpense.expense, expense)
            .fetchJoin()
            .leftJoin(userExpense.tripUser, tripUser)
            .where(getFilterPredicates(filter))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        Long count = queryFactory
            .select(userExpense.count())
            .from(userExpense)
            .leftJoin(userExpense.expense, expense)
            .leftJoin(userExpense.tripUser, tripUser)
            .where(getFilterPredicates(filter))
            .fetchOne();
        if (count == null) throw new BusinessException(ErrorCode.USER_EXPENSE_NOT_FOUND);
        return new PageImpl<>(userExpenseList.stream().map(UserExpenseListRetrieveResponseDto::new).toList(),
                              pageable,
                              count);
    }

    @Override
    public List<CalculationResult> getCalculationResult(Long tripId, ExpenseType expenseType) {
        return queryFactory.select(Projections.constructor(CalculationResult.class,
                                                           tripUser,
                                                           userExpense.amount
                                                               .multiply(tripCurrency.exchangeRate.exchangeRateValue)
                                                               .divide(tripCurrency.exchangeRate.exchangeRateStandard)
                                                               .sum().coalesce(BigDecimal.ZERO)))
            .from(userExpense)
            .leftJoin(userExpense.expense, expense)
            .leftJoin(userExpense.tripUser, tripUser)
            .leftJoin(expense.tripCurrency, tripCurrency)
            .where(expense.trip.id.eq(tripId)
                       .and(expense.expenseType.eq(expenseType)))
            .groupBy(tripUser.id)
            .fetch();
    }


    private Predicate[] getFilterPredicates(UserExpenseFilter filter) {
        return new Predicate[]{
            tripUserIdEq(filter.tripUserId()),
            expenseMethodEq(filter.expenseMethod()),
        };
    }

    private BooleanExpression tripUserIdEq(Long tripUserId) {
        return userExpense.tripUser.id.eq(tripUserId);
    }

    private BooleanExpression expenseMethodEq(ExpenseMethod expenseMethod) {
        return (expenseMethod != null) ? userExpense.expense.expenseMethod.eq(expenseMethod) : null;
    }
}
