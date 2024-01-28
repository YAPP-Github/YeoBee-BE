package com.example.yeobee.core.currency.domain;

import com.example.yeobee.core.country.domain.Country;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class CountryCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "country_name")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "currency_code")
    private Currency currency;
}
