package com.example.yeobee.core.expense.domain;

import com.example.yeobee.core.currency.domain.TripCurrency;
import com.example.yeobee.core.trip.domain.Trip;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

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
    private ExpenseCategory expenseCategory;

    @Enumerated(EnumType.STRING)
    private ExpenseMethod expenseMethod;

    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_code")
    private TripCurrency tripCurrency;

    @OneToMany(mappedBy = "expense")
    private List<ExpensePhoto> expensePhotoList = new ArrayList<>();

    @OneToMany(mappedBy = "expense")
    private List<UserExpense> userExpenseList = new ArrayList<>();
}
