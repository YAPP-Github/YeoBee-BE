package com.example.yeobee.core.expense.domain;

import com.example.yeobee.core.currencyUnit.domain.CurrencyUnit;
import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.userExpense.domain.UserExpense;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private String name;

    private ZonedDateTime payedAt;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ExpenseMethod expenseMethod;

    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private CurrencyUnit unit;

    @OneToMany(mappedBy = "expense")
    private List<ExpensePhoto> expensePhotoList = new ArrayList<>();

    @OneToMany(mappedBy = "expense")
    private List<UserExpense> userExpenseList = new ArrayList<>();
}
