package com.example.yeobee.core.tripUser.domain;

import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.user.domain.User;
import com.example.yeobee.core.userExpense.domain.UserExpense;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "tripUser")
    private List<UserExpense> userExpenseList = new ArrayList<>();
}
