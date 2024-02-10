package com.example.yeobee.core.trip.presentation;

import com.example.yeobee.core.auth.annotation.AuthUser;
import com.example.yeobee.core.trip.application.TripService;
import com.example.yeobee.core.trip.dto.TripResponseDto;
import com.example.yeobee.core.trip.dto.request.CreateTripRequestDto;
import com.example.yeobee.core.trip.dto.request.UpdateTripRequestDto;
import com.example.yeobee.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/trips")
@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public TripResponseDto createTrip(
        @RequestBody CreateTripRequestDto createTripRequest,
        @AuthUser User user
    ) {
        return tripService.createTrip(createTripRequest, user);
    }

    @PutMapping("/{tripId}")
    public TripResponseDto updateTrip(
        @PathVariable long tripId,
        @RequestBody UpdateTripRequestDto updateTripRequest,
        @AuthUser User user
    ) {
        return tripService.updateTrip(tripId, updateTripRequest, user);
    }

    @GetMapping("/me/upcoming")
    public Page<TripResponseDto> getMyUpcomingTrips() {
        return null;
    }

    @GetMapping("/me/past")
    public Page<TripResponseDto> getMyPastTrips() {
        return null;
    }

    @GetMapping("/me/present")
    public Page<TripResponseDto> getMyPresentTrips() {
        return null;
    }

    @GetMapping("/{tripId}")
    public TripResponseDto getTrip(@PathVariable long tripId) {
        return null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{tripId}")
    public void deleteTrip(@PathVariable long tripId, @AuthUser User user) {
        tripService.delete(tripId, user);
    }

    @GetMapping("/date-overlap")
    public Object validateTripDateOverlap() {
        return null;
    }
}
