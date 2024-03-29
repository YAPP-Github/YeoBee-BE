package com.example.yeobee.core.calculation.application;

import com.example.yeobee.common.exception.BusinessException;
import com.example.yeobee.common.exception.ErrorCode;
import com.example.yeobee.core.calculation.domain.CalculationRepository;
import com.example.yeobee.core.calculation.domain.CalculationResult;
import com.example.yeobee.core.calculation.dto.response.BudgetResponseDto;
import com.example.yeobee.core.calculation.dto.response.BudgetResponseDto.IndividualBudget;
import com.example.yeobee.core.calculation.dto.response.BudgetResponseDto.SharedBudget;
import com.example.yeobee.core.calculation.dto.response.Calculation;
import com.example.yeobee.core.calculation.dto.response.TotalExpenseResponseDto;
import com.example.yeobee.core.calculation.dto.response.TripCalculationResponseDto;
import com.example.yeobee.core.expense.domain.ExpenseType;
import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.trip.domain.TripRepository;
import com.example.yeobee.core.trip.domain.TripUser;
import com.example.yeobee.core.trip.domain.TripUserRepository;
import com.example.yeobee.core.user.domain.User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripCalculationService {

    private final TripRepository tripRepository;
    private final CalculationRepository calculationRepository;
    private final TripUserRepository tripUserRepository;

    public TripCalculationResponseDto calculateTrip(Long tripId) {
        tripRepository.findById(tripId).orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
        // 실제 소비한 금액 총합
        List<CalculationResult> sharedCalculationResultList = calculationRepository
            .getCalculationResult(tripId, ExpenseType.SHARED);
        // 공동경비 충전 내역 총합 차감
        sharedCalculationResultList.addAll(setCalculationSumToMinus(calculationRepository.getCalculationResult(
            tripId, ExpenseType.SHARED_BUDGET_INCOME)));
        // 결제자로 등록된 지출 총합 차감
        sharedCalculationResultList.addAll(setCalculationSumToMinus(calculationRepository.getCalculationResult(
            tripId)));
        // 계산 결과 종합
        List<CalculationResult> calculationResultList = mergeCalculationResults(
            sharedCalculationResultList);
        // 최종 정산 가격 계산
        return new TripCalculationResponseDto(calculateBalance(calculationResultList));
    }

    public TotalExpenseResponseDto getTotalExpense(Long tripId) {
        tripRepository.findById(tripId).orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
        // 동행자별 총 지출 계산
        List<CalculationResult> calculationResultList = calculationRepository
            .getTotalExpensePerTripUser(tripId, ExpenseType.SHARED);
        // 남은 공동경비 계산
        Long sharedBudgetIncome = calculationRepository.getTotalBudgetIncome(tripId, ExpenseType.SHARED_BUDGET_INCOME);
        Long sharedBudgetExpense = calculationRepository.getTotalBudgetExpense(tripId, ExpenseType.SHARED, null);
        // 충전한 공동경비가 없을 경우 null
        Long leftSharedBudget = (sharedBudgetIncome == 0L) ? null : sharedBudgetIncome - sharedBudgetExpense;
        return new TotalExpenseResponseDto(calculationResultList, leftSharedBudget);
    }

    private List<CalculationResult> mergeCalculationResults(List<CalculationResult> results) {
        Map<TripUser, Double> summedByTripUserId = results.stream()
            .collect(Collectors.groupingBy(CalculationResult::getTripUser,
                                           Collectors.summingDouble(CalculationResult::getCalculationSum)));

        return summedByTripUserId.entrySet().stream()
            .map(entry -> new CalculationResult(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    public List<Calculation> calculateBalance(List<CalculationResult> results) {
        double average = getAverage(results);
        List<Calculation> calculations = new ArrayList<>();

        // 유저별 평균과의 차이 계산
        for (CalculationResult result : results) {
            double balance = result.getCalculationSum() - average;
            result.setCalculationSum(balance);
        }

        // 정산액에 따라 정렬
        results.sort(Comparator.comparing(CalculationResult::getCalculationSum));

        int i = 0;
        int j = results.size() - 1;

        // 최소 현금 흐름 알고리즘 적용
        while (i < j) {
            double debit = -results.get(i).getCalculationSum();
            double credit = results.get(j).getCalculationSum();

            double min = Math.min(debit, credit);
            results.get(i).addCalculationSum(min);
            results.get(j).addCalculationSum(-min);

            calculations.add(new Calculation(results.get(j).getTripUser(),
                                             results.get(i).getTripUser(),
                                             Math.round(min)));

            if (debit == credit) {
                i++;
                j--;
            } else if (debit < credit) {
                i++;
            } else {
                j--;
            }
        }

        return calculations;
    }

    private List<CalculationResult> setCalculationSumToMinus(List<CalculationResult> results) {
        results.forEach(CalculationResult::setValueMinus);
        return results;
    }

    private double getAverage(List<CalculationResult> results) {
        return results.stream().mapToDouble(CalculationResult::getCalculationSum).average().orElse(0);
    }

    public BudgetResponseDto getBudget(Long tripId, User user) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));

        // totalSharedExpense
        Long totalSharedExpense = calculationRepository.getTotalExpense(tripId, ExpenseType.SHARED);

        // shared
        Long sharedBudgetIncome = calculationRepository.getTotalBudgetIncome(tripId, ExpenseType.SHARED_BUDGET_INCOME);
        Long sharedBudgetExpense = calculationRepository.getTotalBudgetExpense(tripId, ExpenseType.SHARED, null);
        Long leftSharedBudget = (sharedBudgetIncome == 0) ? null : sharedBudgetIncome - sharedBudgetExpense;
        SharedBudget sharedBudget = new SharedBudget(leftSharedBudget,
                                                     sharedBudgetIncome,
                                                     sharedBudgetExpense,
                                                     totalSharedExpense);

        // individual
        TripUser tripUser = tripUserRepository.findByTripAndUser(trip, user)
            .orElseThrow(() -> new BusinessException(ErrorCode.TRIP_ACCESS_UNAUTHORIZED));
        Long individualBudgetIncome = calculationRepository
            .getTotalBudgetIncome(tripId, ExpenseType.INDIVIDUAL_BUDGET_INCOME);
        Long individualBudgetExpense = calculationRepository
            .getTotalBudgetExpense(tripId, ExpenseType.INDIVIDUAL, tripUser);
        Long leftIndividualBudget = (individualBudgetIncome == 0) ?
            null : individualBudgetIncome - individualBudgetExpense;
        IndividualBudget individualBudget = new IndividualBudget(leftIndividualBudget,
                                                                 individualBudgetIncome,
                                                                 individualBudgetExpense);

        return new BudgetResponseDto(sharedBudget, individualBudget);
    }
}