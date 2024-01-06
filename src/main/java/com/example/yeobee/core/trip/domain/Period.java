package com.example.yeobee.core.trip.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.ZonedDateTime;

@Embeddable
@Getter
public class Period {

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;
}
