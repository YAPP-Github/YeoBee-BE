package com.example.yeobee.core.trip.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.expense.domain.ExpenseRepository;
import com.example.yeobee.core.expense.domain.ExpenseType;
import com.example.yeobee.core.expense.domain.UserExpenseRepository;
import com.example.yeobee.core.trip.domain.CalculationResult;
import com.example.yeobee.core.trip.domain.TripRepository;
import com.example.yeobee.core.trip.domain.TripUserRepository;
import com.example.yeobee.core.trip.dto.response.TotalExpenseResponseDto;
import com.example.yeobee.core.trip.dto.response.TripCalculationResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripCalculationService {

    private final UserExpenseRepository userExpenseRepository;
    private final ExpenseRepository expenseRepository;
    private final TripUserRepository tripUserRepository;
    private final TripRepository tripRepository;

    public TripCalculationResponseDto calculateTrip(Long tripId) {
        tripRepository.findById(tripId).orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
        // 실제 소비한 금액 총합
        List<CalculationResult> sharedCalculationResultList = userExpenseRepository
            .getCalculationResult(tripId, ExpenseType.SHARED);
        // 공동경비 충전 내역 총합 차감
        sharedCalculationResultList.addAll(CalculationResult.setCalculationSumToMinus(userExpenseRepository.getCalculationResult(
            tripId, ExpenseType.SHARED_BUDGET_INCOME)));
        // 결제자로 등록된 지출 총합 차감
        sharedCalculationResultList.addAll(CalculationResult.setCalculationSumToMinus(expenseRepository.getCalculationResult(
            tripId)));
        // 계산 결과 종합
        List<CalculationResult> calculationResultList = CalculationResult.mergeCalculationResults(
            sharedCalculationResultList);
        // 최종 정산 가격 계산
        return new TripCalculationResponseDto(CalculationResult.calculateBalance(calculationResultList));
    }

    public TotalExpenseResponseDto getTotalExpense(Long tripId) {
        tripRepository.findById(tripId).orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
        // 동행자별 총 지출 계산
        List<CalculationResult> calculationResultList = tripUserRepository
            .getTotalExpensePerTripUser(tripId, ExpenseType.SHARED);
        // 남은 공동경비 계산
        Long leftSharedBudget = expenseRepository.getTotalSharedBudgetIncome()
                                - expenseRepository.getTotalSharedBudgetExpense();
        return new TotalExpenseResponseDto(calculationResultList, leftSharedBudget);
    }
}