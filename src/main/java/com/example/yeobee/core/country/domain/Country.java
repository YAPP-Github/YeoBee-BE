package com.example.yeobee.core.country.domain;

import com.example.yeobee.core.currencyUnit.domain.CurrencyUnit;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
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
}
