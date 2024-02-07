package com.example.yeobee.core.trip.presentation;

import com.example.yeobee.core.trip.application.TripCalculationService;
import com.example.yeobee.core.trip.dto.response.TotalExpenseResponseDto;
import com.example.yeobee.core.trip.dto.response.TripCalculationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripCalculationService tripCalculationService;

    @GetMapping(value = "/{tripId}/calculation")
    public ResponseEntity<TripCalculationResponseDto> getTripCalculationResult(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripCalculationService.calculateTrip(tripId));
    }

    @GetMapping(value = "/{tripId}/total-expense")
    public ResponseEntity<TotalExpenseResponseDto> getTotalExpense(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripCalculationService.getTotalExpense(tripId));
    }
}
