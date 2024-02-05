package com.example.yeobee.core.country.domain;

import com.example.yeobee.core.currency.domain.CountryCurrency;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Country {

    @Id
    private String name;

    private String flagImageUrl;

    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    private Continent continent;

    @OneToMany(mappedBy = "country")
    private List<CountryCurrency> countryCurrencyList = new ArrayList<>();

    public Country(String name) {
        this.name = name;
    }

    public Country(String name, String flagImageUrl, String coverImageUrl, Continent continent) {
        this.name = name;
        this.flagImageUrl = flagImageUrl;
        this.coverImageUrl = coverImageUrl;
        this.continent = continent;
    }
}
