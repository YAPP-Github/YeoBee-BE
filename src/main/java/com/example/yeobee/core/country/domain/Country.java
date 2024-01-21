package com.example.yeobee.core.country.domain;

import com.example.yeobee.core.currency.domain.CountryCurrency;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Country {

    @Id
    private String name;

    private String flagImageUrl;

    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    private Continent continent;

    @OneToMany(mappedBy = "country")
    private List<TripCountry> tripCountryList = new ArrayList<>();

    @OneToMany(mappedBy = "country")
    private List<CountryCurrency> countryCurrencyList = new ArrayList<>();
}
