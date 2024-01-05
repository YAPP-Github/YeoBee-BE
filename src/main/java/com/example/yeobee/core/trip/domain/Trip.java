package com.example.yeobee.core.trip.domain;

import com.example.yeobee.common.entity.BaseEntity;
import com.example.yeobee.core.expense.domain.Expense;
import com.example.yeobee.core.tripUser.domain.TripUser;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Trip extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Embedded
    private Period period;

    @Embedded
    private Budget budget;

    @OneToMany(mappedBy = "trip")
    private List<Expense> expenseList = new ArrayList<>();

    @OneToMany(mappedBy = "trip")
    private List<TripUser> tripUserList = new ArrayList<>();
}
