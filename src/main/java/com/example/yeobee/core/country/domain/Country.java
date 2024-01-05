package com.example.yeobee.core.country.domain;

import com.example.yeobee.core.currencyUnit.domain.CurrencyUnit;
import com.example.yeobee.core.tripCountry.domain.TripCountry;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String flagImageUrl;

    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    private Continent continent;

    @OneToMany(mappedBy = "country")
    private List<CurrencyUnit> currencyUnitList = new ArrayList<>();

    @OneToMany(mappedBy = "country")
    private List<TripCountry> tripCountryList = new ArrayList<>();
}
