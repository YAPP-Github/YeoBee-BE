package com.example.yeobee.core.expense.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.currency.domain.TripCurrency;
import com.example.yeobee.core.currency.domain.TripCurrencyRepository;
import com.example.yeobee.core.expense.domain.Expense;
import com.example.yeobee.core.expense.domain.ExpenseRepository;
import com.example.yeobee.core.expense.dto.request.ExpenseCreateRequestDto;
import com.example.yeobee.core.expense.dto.request.ExpenseImage;
import com.example.yeobee.core.expense.dto.request.Payer;
import com.example.yeobee.core.expense.dto.response.ExpenseCreateResponseDto;
import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.trip.domain.TripRepository;
import com.example.yeobee.core.trip.domain.TripUser;
import com.example.yeobee.core.trip.domain.TripUserRepository;
import jakarta.transaction.Transactional;
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
        // trip 에 expense 추가
        Trip trip = tripRepository.findById(request.tripId())
            .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
        trip.addExpense(expense);
        tripRepository.save(trip);
        // tripCurrency 에 expense 추가
        TripCurrency tripCurrency = tripCurrencyRepository.findByTripIdAndCurrencyCode(trip.getId(),
                                                                                       request.currencyCode())
            .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_CURRENCY_NOT_FOUND));
        tripCurrency.addExpense(expense);
        tripCurrencyRepository.save(tripCurrency);
        // tripUser 에 userExpense 추가
        request.payerList().forEach((payer) -> {
            TripUser tripUser = tripUserRepository.findById(payer.tripUserId()).orElseThrow(() -> new BusinessException(
                ErrorCode.TRIP_USER_NOT_FOUND));
            expense.getUserExpenseList()
                .stream()
                .filter((e) -> e.getTripUser().getId().equals(tripUser.getId()))
                .forEach(
                    tripUser::addUserExpense);
            tripUserRepository.save(tripUser);
        });
        // payer 에 expense 추가
        if (request.payerId() != null) {
            TripUser payer = tripUserRepository.findById(request.payerId()).orElseThrow(() -> new BusinessException(
                ErrorCode.TRIP_USER_NOT_FOUND));
            payer.addExpense(expense);
            tripUserRepository.save(payer);
        }
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
                                                Payer::new).toList(),
                                            expense.getExpensePhotoList().stream().map(ExpenseImage::new).toList());
    }
}
