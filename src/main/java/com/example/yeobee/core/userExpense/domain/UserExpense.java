package com.example.yeobee.core.userExpense.domain;

import com.example.yeobee.core.expense.domain.Expense;
import com.example.yeobee.core.tripUser.domain.TripUser;
import jakarta.persistence.*;

@Entity
public class UserExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "trip_user_id")
    private TripUser tripUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "expense_id")
    private Expense expense;
}
