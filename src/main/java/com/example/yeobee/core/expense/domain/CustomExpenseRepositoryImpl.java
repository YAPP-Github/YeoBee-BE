package com.example.yeobee.core.expense.domain;

import static com.example.yeobee.core.expense.domain.QExpense.expense;
import static com.example.yeobee.core.trip.domain.QTrip.trip;
import static com.example.yeobee.core.trip.domain.QTripCurrency.tripCurrency;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.expense.dto.request.ExpenseListFilter;
import com.example.yeobee.core.expense.dto.response.ExpenseListRetrieveResponseDto;
import com.example.yeobee.core.trip.domain.Trip;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


@RequiredArgsConstructor
public class CustomExpenseRepositoryImpl implements CustomExpenseRepository {

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
        // 여행 전, 중, 후 분기처리
        Predicate payedAtPredicate = null;
        if (filter.payedAt() != null) {
            if (filter.payedAt().isBefore(filter.trip().getPeriod().getStartDate())) {
                payedAtPredicate = payedAtLt(filter.payedAt());
            } else if (filter.payedAt().isAfter(filter.trip().getPeriod().getEndDate())) {
                payedAtPredicate = payedAtGoe(filter.payedAt());
            } else {
                payedAtPredicate = payedAtEq(filter.payedAt());
            }
        }

        return new Predicate[]{
            tripIdEq(filter.trip()),
            expenseTypeEq(filter.expenseType()),
            payedAtPredicate,
            expenseMethodEq(filter.expenseMethod()),
            currencyCodeEq(filter.currencyCode())
        };
    }

    private BooleanExpression tripIdEq(Trip trip) {
        return trip != null ? expense.trip.eq(trip) : null;
    }

    private BooleanExpression expenseTypeEq(ExpenseType expenseType) {
        if (expenseType != null) {
            switch (expenseType) {
                case SHARED_ALL -> {
                    return expense.expenseType.eq(ExpenseType.SHARED)
                        .or(expense.expenseType.eq(ExpenseType.SHARED_BUDGET_INCOME));
                }
                case INDIVIDUAL_ALL -> {
                    return expense.expenseType.eq(ExpenseType.INDIVIDUAL)
                        .or(expense.expenseType.eq(ExpenseType.INDIVIDUAL_BUDGET_INCOME));
                }
                case SHARED_BUDGET_EXPENSE -> {
                    return expense.expenseType.eq(ExpenseType.SHARED)
                        .and(expense.payer.isNull());
                }
                default -> {
                    return expense.expenseType.eq(expenseType);
                }
            }
        }
        return null;
    }

    private BooleanExpression payedAtEq(LocalDate payedAt) {
        LocalDateTime startOfDay = payedAt.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return expense.payedAt.goe(startOfDay).and(expense.payedAt.lt(endOfDay));
    }

    private BooleanExpression payedAtGoe(LocalDate payedAt) {
        LocalDateTime startOfDay = payedAt.atStartOfDay();
        return expense.payedAt.goe(startOfDay);
    }

    private BooleanExpression payedAtLt(LocalDate payedAt) {
        LocalDateTime endOfDay = payedAt.atStartOfDay().plusDays(1);
        return expense.payedAt.lt(endOfDay);
    }

    private BooleanExpression expenseMethodEq(ExpenseMethod expenseMethod) {
        return expenseMethod != null ? expense.expenseMethod.eq(expenseMethod) : null;
    }

    private BooleanExpression currencyCodeEq(String currencyCode) {
        return currencyCode != null ? expense.tripCurrency.currency.code.eq(currencyCode) : null;
    }
}
