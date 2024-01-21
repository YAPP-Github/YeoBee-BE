package com.example.yeobee.core.currency.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Currency {

    @Id
    private String code;

    private String name;

    @OneToMany(mappedBy = "currency")
    private List<CountryCurrency> countryCurrencyList = new ArrayList<>();

    @OneToMany(mappedBy = "currency")
    private List<TripCurrency> tripCurrencyList = new ArrayList<>();
}
