package com.example.yeobee.core.currencyUnit.domain;

import com.example.yeobee.core.country.domain.Country;
import com.example.yeobee.core.expense.domain.Expense;
import com.example.yeobee.core.tripCountry.domain.TripCountry;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class CurrencyUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String symbol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "unit")
    private List<Expense> expenseList = new ArrayList<>();

    @OneToMany(mappedBy = "currencyUnit")
    private List<TripCountry> tripCountryList = new ArrayList<>();
}
