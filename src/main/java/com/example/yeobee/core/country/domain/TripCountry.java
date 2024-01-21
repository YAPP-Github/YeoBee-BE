package com.example.yeobee.core.country.domain;

import com.example.yeobee.core.trip.domain.Trip;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class TripCountry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_name")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
