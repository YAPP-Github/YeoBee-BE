package com.example.yeobee.core.currency.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Currency {

    @Id
    private String code;

    private String name;

    @Embedded
    private ExchangeRate exchangeRate;
}
