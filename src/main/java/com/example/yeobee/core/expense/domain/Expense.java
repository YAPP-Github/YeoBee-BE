package com.example.yeobee.core.expense.domain;

import com.example.yeobee.core.trip.domain.Trip;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private String name;

    private LocalDate payedAt;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Method method;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @Column(name = "trip_id")
    private Trip trip;

    @OneToMany(mappedBy = "expense")
    private List<ExpensePhoto> expensePhotoList = new ArrayList<>();
}
