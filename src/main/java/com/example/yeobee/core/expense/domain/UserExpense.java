package com.example.yeobee.core.expense.domain;

import com.example.yeobee.core.trip.domain.TripUser;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class UserExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_user_id")
    private TripUser tripUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private Expense expense;
}
