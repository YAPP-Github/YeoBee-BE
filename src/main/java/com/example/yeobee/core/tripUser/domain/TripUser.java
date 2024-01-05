package com.example.yeobee.core.tripUser.domain;

import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.user.domain.User;
import jakarta.persistence.*;

@Entity
public class TripUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "trip_id")
    private Trip trip;
}
