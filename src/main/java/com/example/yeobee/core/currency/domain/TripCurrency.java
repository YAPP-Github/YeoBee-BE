package com.example.yeobee.core.currency.domain;

import com.example.yeobee.core.expense.domain.Expense;
import com.example.yeobee.core.trip.domain.Trip;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@Entity
public class TripCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ExchangeRate exchangeRate;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "currency_code")
    private Currency currency;

    @OneToMany(mappedBy = "tripCurrency")
    private List<Expense> expenseList = new ArrayList<>();

    public void addExpense(Expense expense) {
        expenseList.add(expense);
        if (expense.getTripCurrency() != this) {
            expense.setTripCurrency(this);
        }
    }
}
