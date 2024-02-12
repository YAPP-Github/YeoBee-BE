package com.example.yeobee.core.calculation.presentation;

import com.example.yeobee.core.auth.annotation.AuthUser;
import com.example.yeobee.core.calculation.application.TripCalculationService;
import com.example.yeobee.core.calculation.dto.response.BudgetResponseDto;
import com.example.yeobee.core.calculation.dto.response.TotalExpenseResponseDto;
import com.example.yeobee.core.calculation.dto.response.TripCalculationResponseDto;
import com.example.yeobee.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/trips")
@RequiredArgsConstructor
public class TripCalculationController {

    private final TripCalculationService tripCalculationService;

    @GetMapping(value = "/{tripId}/calculation")
    public ResponseEntity<TripCalculationResponseDto> getTripCalculationResult(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripCalculationService.calculateTrip(tripId));
    }

    @GetMapping(value = "/{tripId}/total-expense")
    public ResponseEntity<TotalExpenseResponseDto> getTotalExpense(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripCalculationService.getTotalExpense(tripId));
    }

    @GetMapping(value = "/{tripId}/budget")
    public ResponseEntity<BudgetResponseDto> getBudget(@PathVariable Long tripId, @AuthUser User user) {
        return ResponseEntity.ok(tripCalculationService.getBudget(tripId, user));
    }
}
