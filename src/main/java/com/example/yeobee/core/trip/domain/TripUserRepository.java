package com.example.yeobee.core.trip.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripUserRepository extends JpaRepository<TripUser, Long> {

    Optional<TripUser> findByTripIdAndUserId(Long tripId, Long UserId);
}
