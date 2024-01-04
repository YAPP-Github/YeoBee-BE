package com.example.yeobee.core.trip.domain;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class Period {

    private LocalDate startDate;

    private LocalDate endDate;
}
