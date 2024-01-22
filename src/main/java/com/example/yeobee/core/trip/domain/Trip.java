package com.example.yeobee.core.trip.domain;

import com.example.yeobee.common.entity.BaseEntity;
import com.example.yeobee.core.country.domain.TripCountry;
import com.example.yeobee.core.currency.domain.TripCurrency;
import com.example.yeobee.core.expense.domain.Expense;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Trip extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Embedded
    private Period period;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Expense> expenseList = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<TripUser> tripUserList = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<TripCountry> tripCountryList = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<TripCurrency> tripCurrencyList = new ArrayList<>();

    public void addExpense(Expense expense) {
        expenseList.add(expense);
        if (expense.getTrip() != this) {
            expense.setTrip(this);
        }
    }
}
