package com.example.yeobee.core.expense.domain;

import static com.example.yeobee.core.expense.domain.QExpense.expense;
import static com.example.yeobee.core.expense.domain.QUserExpense.userExpense;
import static com.example.yeobee.core.trip.domain.QTripUser.tripUser;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.expense.dto.request.UserExpenseFilter;
import com.example.yeobee.core.expense.dto.response.UserExpenseListRetrieveResponseDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class UserExpenseRepositoryImpl implements CustomUserExpenseRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserExpenseListRetrieveResponseDto> findByFilter(UserExpenseFilter filter, Pageable pageable) {
        List<UserExpense> userExpenseList = queryFactory
            .selectFrom(userExpense)
            .leftJoin(userExpense.expense, expense)
            .fetchJoin()
            .leftJoin(userExpense.tripUser, tripUser)
            .where(getPredicates(filter))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        Long count = queryFactory
            .select(userExpense.count())
            .from(userExpense)
            .leftJoin(userExpense.expense, expense)
            .leftJoin(userExpense.tripUser, tripUser)
            .where(getPredicates(filter))
            .fetchOne();
        if (count == null) throw new BusinessException(ErrorCode.USER_EXPENSE_NOT_FOUND);
        return new PageImpl<>(userExpenseList.stream().map(UserExpenseListRetrieveResponseDto::new).toList(),
                              pageable,
                              count);
    }

    private Predicate[] getPredicates(UserExpenseFilter filter) {
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
