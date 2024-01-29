package com.example.yeobee.core.expense.domain;

import static com.example.yeobee.core.currency.domain.QTripCurrency.tripCurrency;
import static com.example.yeobee.core.expense.domain.QExpense.expense;
import static com.example.yeobee.core.trip.domain.QTrip.trip;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.expense.dto.request.ExpenseListFilter;
import com.example.yeobee.core.expense.dto.response.ExpenseListRetrieveResponseDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements CustomExpenseRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ExpenseListRetrieveResponseDto> findByFilter(ExpenseListFilter filter, Pageable pageable) {
        List<Expense> expenseList = queryFactory
            .selectFrom(expense)
            .leftJoin(expense.tripCurrency, tripCurrency)
            .fetchJoin()
            .leftJoin(expense.trip, trip)
            .where(getPredicates(filter))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        Long count = queryFactory
            .select(expense.count())
            .from(expense)
            .leftJoin(expense.tripCurrency, tripCurrency)
            .leftJoin(expense.trip, trip)
            .where(getPredicates(filter))
            .fetchOne();
        if (count == null) throw new BusinessException(ErrorCode.EXPENSE_NOT_FOUND);
        return new PageImpl<>(expenseList.stream().map(ExpenseListRetrieveResponseDto::new).toList(), pageable, count);
    }

    private Predicate[] getPredicates(ExpenseListFilter filter) {
        return new Predicate[]{
            tripIdEq(filter.tripId()),
            expenseTypeEq(filter.expenseType()),
            payedAtEq(filter.payedAt()),
            expenseMethodEq(filter.expenseMethod()),
            currencyCodeEq(filter.currencyCode())
        };
    }

    private BooleanExpression tripIdEq(Long tripId) {
        return tripId != null ? expense.trip.id.eq(tripId) : null;
    }

    private BooleanExpression expenseTypeEq(ExpenseType expenseType) {
        return expenseType != null ? expense.expenseType.eq(expenseType) : null;
    }

    private BooleanExpression payedAtEq(LocalDate payedAt) {
        if (payedAt != null) {
            LocalDateTime startOfDay = payedAt.atStartOfDay();
            LocalDateTime endOfDay = payedAt.atTime(LocalTime.MAX);
            return expense.payedAt.between(startOfDay, endOfDay);
        }
        return null;
    }

    private BooleanExpression expenseMethodEq(ExpenseMethod expenseMethod) {
        return expenseMethod != null ? expense.expenseMethod.eq(expenseMethod) : null;
    }

    private BooleanExpression currencyCodeEq(String currencyCode) {
        return currencyCode != null ? expense.tripCurrency.currency.code.eq(currencyCode) : null;
    }
}
