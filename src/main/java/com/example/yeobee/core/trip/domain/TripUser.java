package com.example.yeobee.core.trip.domain;

import com.example.yeobee.core.expense.domain.UserExpense;
import com.example.yeobee.core.user.domain.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class TripUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TripUserState tripUserState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToMany(mappedBy = "tripUser")
    private List<UserExpense> userExpenseList = new ArrayList<>();
}
