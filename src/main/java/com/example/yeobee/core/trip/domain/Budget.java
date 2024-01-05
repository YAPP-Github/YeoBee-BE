package com.example.yeobee.core.trip.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Budget {

    private Long individualBudget;

    private Long sharedBudget;
}
