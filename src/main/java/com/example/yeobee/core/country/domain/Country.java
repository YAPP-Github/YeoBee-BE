package com.example.yeobee.core.country.domain;

import jakarta.persistence.*;

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
}
