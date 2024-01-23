package com.example.yeobee.core.expense.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.currency.domain.TripCurrency;
import com.example.yeobee.core.currency.domain.TripCurrencyRepository;
import com.example.yeobee.core.expense.domain.Expense;
import com.example.yeobee.core.expense.domain.ExpenseRepository;
import com.example.yeobee.core.expense.domain.UserExpense;
import com.example.yeobee.core.expense.dto.common.ExpensePhotoDto;
import com.example.yeobee.core.expense.dto.common.UserExpenseDto;
import com.example.yeobee.core.expense.dto.request.ExpenseCreateRequestDto;
import com.example.yeobee.core.expense.dto.request.ExpenseUpdateRequestDto;
import com.example.yeobee.core.expense.dto.response.ExpenseCreateResponseDto;
import com.example.yeobee.core.expense.dto.response.ExpenseDetailRetrieveResponseDto;
import com.example.yeobee.core.expense.dto.response.ExpenseUpdateResponseDto;
import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.trip.domain.TripRepository;
import com.example.yeobee.core.trip.domain.TripUser;
import com.example.yeobee.core.trip.domain.TripUserRepository;
import com.example.yeobee.core.user.domain.User;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;
    private final TripUserRepository tripUserRepository;
    private final TripCurrencyRepository tripCurrencyRepository;

    @Transactional
    public ExpenseCreateResponseDto createExpense(ExpenseCreateRequestDto request) {
        Expense expense = new Expense(request);
        expenseRepository.save(expense);

        Trip trip = setTrip(request.tripId(), expense);
        TripCurrency tripCurrency = setTripCurrency(request.tripId(), request.currencyCode(), expense);
        setUserExpense(request.payerList(), expense);
        setPayer(request.payerId(), expense);

        return new ExpenseCreateResponseDto(expense.getId(),
                                            trip.getId(),
                                            expense.getPayedAt(),
                                            expense.getExpenseType(),
                                            expense.getAmount(),
                                            tripCurrency.getCurrency().getCode(),
                                            expense.getExpenseMethod(),
                                            expense.getName(),
                                            expense.getPayer().getId(),
                                            expense.getUserExpenseList().stream().map(
                                                UserExpenseDto::new).toList(),
                                            expense.getExpensePhotoList().stream().map(ExpensePhotoDto::new).toList());
    }

    @Transactional
    public ExpenseUpdateResponseDto updateExpense(Long expenseId, ExpenseUpdateRequestDto request) {
        Expense expense = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new BusinessException(ErrorCode.EXPENSE_NOT_FOUND));

        expense.update(request);
        Trip trip = setTrip(request.tripId(), expense);
        TripCurrency tripCurrency = setTripCurrency(request.tripId(), request.currencyCode(), expense);
        setUserExpense(request.payerList(), expense);
        setPayer(request.payerId(), expense);

        return new ExpenseUpdateResponseDto(expense.getId(),
                                            trip.getId(),
                                            expense.getExpenseType(),
                                            expense.getAmount(),
                                            tripCurrency.getCurrency().getCode(),
                                            expense.getExpenseMethod(),
                                            expense.getName(),
                                            expense.getPayer().getId(),
                                            expense.getUserExpenseList().stream().map(
                                                UserExpenseDto::new).toList(),
                                            expense.getExpensePhotoList().stream().map(ExpensePhotoDto::new).toList());
    }

    public void deleteExpense(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
            .orElseThrow(() -> new BusinessException(ErrorCode.EXPENSE_NOT_FOUND));
        expenseRepository.delete(expense);
    }

    private Trip setTrip(Long tripId, Expense expense) {
        if (!tripId.equals(expense.getTrip().getId())) {
            Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
            trip.addExpense(expense);
            return tripRepository.save(trip);
        }
        return expense.getTrip();
    }

    private TripCurrency setTripCurrency(Long tripId, String currencyCode, Expense expense) {
        if (!currencyCode.equals(expense.getTripCurrency().getCurrency().getCode())) {
            TripCurrency tripCurrency = tripCurrencyRepository.findByTripIdAndCurrencyCode(tripId, currencyCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_CURRENCY_NOT_FOUND));
            tripCurrency.addExpense(expense);
            return tripCurrencyRepository.save(tripCurrency);
        }
        return expense.getTripCurrency();
    }

    private void setUserExpense(List<UserExpenseDto> payerList, Expense expense) {
        payerList.forEach((payer) -> {
            TripUser tripUser = tripUserRepository.findById(payer.tripUserId()).orElseThrow(() -> new BusinessException(
                ErrorCode.TRIP_USER_NOT_FOUND));
            UserExpense userExpense = new UserExpense(payer);
            tripUser.addUserExpense(userExpense);
            tripUserRepository.save(tripUser);
            expense.addUserExpense(userExpense);
            expenseRepository.save(expense);
        });
    }

    private void setPayer(Long payerId, Expense expense) {
        if (payerId != null) {
            TripUser payer = tripUserRepository.findById(payerId).orElseThrow(() -> new BusinessException(
                ErrorCode.TRIP_USER_NOT_FOUND));
            payer.addExpense(expense);
            tripUserRepository.save(payer);
        } else {
            expense.setPayer(null);
        }
    }
}
