package com.example.yeobee.core.trip.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Budget {

    private Long individualBudget;

    private Long sharedBudget;
}
