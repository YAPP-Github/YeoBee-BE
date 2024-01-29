package com.example.yeobee.core.expense.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.currency.domain.TripCurrency;
import com.example.yeobee.core.currency.domain.TripCurrencyRepository;
import com.example.yeobee.core.expense.domain.Expense;
import com.example.yeobee.core.expense.domain.ExpenseRepository;
import com.example.yeobee.core.expense.domain.UserExpense;
import com.example.yeobee.core.expense.domain.UserExpenseRepository;
import com.example.yeobee.core.expense.dto.UserExpenseDto;
import com.example.yeobee.core.expense.dto.request.*;
import com.example.yeobee.core.expense.dto.response.ExpenseCreateResponseDto;
import com.example.yeobee.core.expense.dto.response.ExpenseDetailRetrieveResponseDto;
import com.example.yeobee.core.expense.dto.response.ExpenseListRetrieveResponseDto;
import com.example.yeobee.core.expense.dto.response.ExpenseUpdateResponseDto;
import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.trip.domain.TripRepository;
import com.example.yeobee.core.trip.domain.TripUser;
import com.example.yeobee.core.trip.domain.TripUserRepository;
import com.example.yeobee.core.user.domain.User;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;
    private final TripUserRepository tripUserRepository;
    private final TripCurrencyRepository tripCurrencyRepository;
    private final UserExpenseRepository userExpenseRepository;

    @Transactional
    public ExpenseCreateResponseDto createExpense(ExpenseCreateRequestDto request) {
        Expense expense = new Expense(request);
        createOrUpdateExpense(expense, new CreateOrUpdateExpenseDto(request));
        expenseRepository.save(expense);
        return new ExpenseCreateResponseDto(expense);
    }

    @Transactional
    public ExpenseUpdateResponseDto updateExpense(Long expenseId, ExpenseUpdateRequestDto request) {
        Expense expense = findExpenseById(expenseId);
        expense.update(request);
        createOrUpdateExpense(expense, new CreateOrUpdateExpenseDto(request));
        expenseRepository.save(expense);
        return new ExpenseUpdateResponseDto(expense);
    }

    @Transactional
    public void deleteExpense(Long expenseId) {
        Expense expense = findExpenseById(expenseId);
        expenseRepository.delete(expense);
    }

    public ExpenseDetailRetrieveResponseDto retrieveExpenseDetail(Long expenseId, User user) {
        Expense expense = findExpenseById(expenseId);
        TripUser userTripUser = tripUserRepository.findByTripIdAndUserId(expense.getTrip().getId(), user.getId())
            .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_ACCESS_UNAUTHORIZED));
        return new ExpenseDetailRetrieveResponseDto(expense, userTripUser.getId());
    }

    public Page<ExpenseListRetrieveResponseDto> retrieveExpenseList(ExpenseListRetrieveRequestDto request) {
        if (request.pageIndex() == null || request.pageSize() == null || request.tripId() == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        return expenseRepository.findByFilter(new ExpenseListFilter(request),
                                              PageRequest.of(request.pageIndex(), request.pageSize()));
    }

    private void createOrUpdateExpense(Expense expense, CreateOrUpdateExpenseDto dto) {
        setTrip(dto.tripId(), expense);
        setTripCurrency(dto.tripId(), dto.currencyCode(), expense);
        setUserExpense(dto.payerList(), expense);
        setPayer(dto.payerId(), expense);
    }

    private void setTrip(Long tripId, Expense expense) {
        if (expense.getTrip() == null || !tripId.equals(expense.getTrip().getId())) {
            Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
            expense.setTrip(trip);
        }
    }

    private void setTripCurrency(Long tripId, String currencyCode, Expense expense) {
        if (expense.getTripCurrency() == null || !currencyCode.equals(expense.getTripCurrency()
                                                                          .getCurrency()
                                                                          .getCode())) {
            TripCurrency tripCurrency = tripCurrencyRepository.findByTripIdAndCurrencyCode(tripId, currencyCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_CURRENCY_NOT_FOUND));
            expense.setTripCurrency(tripCurrency);
        }
    }

    private void setUserExpense(List<UserExpenseDto> payerList, Expense expense) {
        payerList.forEach((payer) -> {
            TripUser tripUser = tripUserRepository.findById(payer.tripUserId()).orElseThrow(() -> new BusinessException(
                ErrorCode.TRIP_USER_NOT_FOUND));
            UserExpense userExpense = new UserExpense(payer, tripUser, expense);
            userExpenseRepository.save(userExpense);
        });
    }

    private void setPayer(Long payerId, Expense expense) {
        if (payerId != null) {
            TripUser payer = tripUserRepository.findById(payerId).orElseThrow(() -> new BusinessException(
                ErrorCode.TRIP_USER_NOT_FOUND));
            expense.setPayer(payer);
        } else {
            expense.setPayer(null);
        }
    }

    private Expense findExpenseById(Long expenseId) {
        return expenseRepository.findById(expenseId)
            .orElseThrow(() -> new BusinessException(ErrorCode.EXPENSE_NOT_FOUND));
    }
}
