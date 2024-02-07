package com.example.yeobee.core.currency.domain;

import com.example.yeobee.core.country.domain.Country;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "country_currency_unique", columnNames = {"country_name", "currency_code"})
})
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

    public CountryCurrency(Country country, Currency currency) {
        this.country = country;
        this.currency = currency;
    }
}
