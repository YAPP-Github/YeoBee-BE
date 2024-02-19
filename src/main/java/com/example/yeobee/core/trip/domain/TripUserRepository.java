package com.example.yeobee.core.trip.domain;

import com.example.yeobee.core.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripUserRepository extends JpaRepository<TripUser, Long> {

    Optional<TripUser> findByTripAndUser(Trip trip, User user);

    @Query("select count(tu) from TripUser tu where tu.user.id = :userId")
    Integer getTripCountByUserId(Long userId);
}
