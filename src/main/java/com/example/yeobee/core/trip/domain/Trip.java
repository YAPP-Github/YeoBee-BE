package com.example.yeobee.core.trip.domain;

import com.example.yeobee.common.entity.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Trip extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Embedded
    private Period period;

    @Embedded
    private Budget budget;
}
