package com.example.yeobee.core.trip.domain;

import com.example.yeobee.core.trip.dto.response.Calculation;
import com.example.yeobee.core.trip.dto.response.CalculationUser;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CalculationResult {

    private TripUser tripUser;
    private Double calculationSum;


    public static List<CalculationResult> mergeCalculationResults(List<CalculationResult> results) {
        Map<TripUser, Double> summedByTripUserId = results.stream()
            .collect(Collectors.groupingBy(e -> e.tripUser,
                                           Collectors.summingDouble(CalculationResult::getCalculationSum)));

        return summedByTripUserId.entrySet().stream()
            .map(entry -> new CalculationResult(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    public static List<Calculation> calculateBalance(List<CalculationResult> results) {
        double average = getAverage(results);
        results.sort(Comparator.comparing(CalculationResult::getCalculationSum).reversed());
        // 평균과의 차이 계산 후, 가장 큰 값을 갖는 객체(가장 많은 돈을 소비한 사람)부터 내야 할 돈 계산 후, 다음 사람으로 넘김
        List<Calculation> calculationList = new ArrayList<>();
        for (int i = 0; i < results.size() - 1; i++) {
            TripUser sender = results.get(i).getTripUser();
            TripUser receiver = results.get(i + 1).getTripUser();
            double amount = results.get(i).calculationSum - average;
            calculationList.add(new Calculation(new CalculationUser(sender), new CalculationUser(receiver),
                                                Math.round(amount)));
            results.get(i + 1).calculationSum += amount;
        }
        return calculationList;
    }

    public static List<CalculationResult> setCalculationSumToMinus(List<CalculationResult> results) {
        results.forEach(CalculationResult::setValueMinus);
        return results;
    }

    private void setValueMinus() {
        calculationSum *= -1;
    }

    public static double getAverage(List<CalculationResult> results) {
        return results.stream().mapToDouble(e -> e.calculationSum).average().orElse(0);
    }
}
