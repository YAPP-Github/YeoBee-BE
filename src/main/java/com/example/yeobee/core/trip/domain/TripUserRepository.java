package com.example.yeobee.core.trip.domain;

import com.example.yeobee.core.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripUserRepository extends JpaRepository<TripUser, Long>, CustomTripUserRepository {

    Optional<TripUser> findByTripAndUser(Trip trip, User user);
}
