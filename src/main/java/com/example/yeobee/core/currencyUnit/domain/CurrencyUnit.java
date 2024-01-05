package com.example.yeobee.core.currencyUnit.domain;

import com.example.yeobee.core.country.domain.Country;
import jakarta.persistence.*;

@Entity
public class CurrencyUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String symbol;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "country_id")
    private Country country;
}
