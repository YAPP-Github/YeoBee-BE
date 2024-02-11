package com.example.yeobee.core.trip.domain;

import com.example.yeobee.core.user.domain.User;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT tu.trip FROM TripUser tu WHERE tu.user = :user AND tu.trip.period.endDate < :today ORDER BY tu.trip.period.startDate DESC, tu.trip.period.endDate DESC")
    Page<Trip> findPastTrips(User user, LocalDate today, Pageable pageable);

    @Query("SELECT tu.trip FROM TripUser tu WHERE tu.user = :user AND tu.trip.period.startDate <= :today AND tu.trip.period.endDate >= :today ORDER BY tu.trip.createdAt ASC")
    Page<Trip> findPresentTrips(User user, LocalDate today, Pageable pageable);

    @Query("SELECT tu.trip FROM TripUser tu WHERE tu.user = :user AND tu.trip.period.startDate > :today ORDER BY tu.trip.period.startDate ASC, tu.trip.period.endDate ASC")
    Page<Trip> findFutureTrips(User user, LocalDate today, Pageable pageable);
}
