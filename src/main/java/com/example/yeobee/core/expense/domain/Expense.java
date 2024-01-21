package com.example.yeobee.core.expense.domain;

import com.example.yeobee.core.currency.domain.TripCurrency;
import com.example.yeobee.core.expense.dto.request.ExpenseCreateRequestDto;
import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.trip.domain.TripUser;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private String name;

    private ZonedDateTime payedAt;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;

    @Enumerated(EnumType.STRING)
    private ExpenseMethod expenseMethod;

    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_currency_id")
    private TripCurrency tripCurrency;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id")
    private TripUser payer;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<ExpensePhoto> expensePhotoList = new ArrayList<>();

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<UserExpense> userExpenseList = new ArrayList<>();

    public Expense(ExpenseCreateRequestDto requestDto) {
        amount = requestDto.amount();
        name = requestDto.name();
        payedAt = requestDto.payedAt();
        expenseCategory = requestDto.expenseCategory();
        expenseMethod = requestDto.expenseMethod();
        expenseType = requestDto.expenseType();
        requestDto.imageList().forEach((e) -> addExpensePhoto(new ExpensePhoto(e)));
        requestDto.payerList().forEach((e) -> addUserExpense(new UserExpense(e)));
    }

    private void addExpensePhoto(ExpensePhoto expensePhoto) {
        expensePhotoList.add(expensePhoto);
        if (expensePhoto.getExpense() == null) {
            expensePhoto.setExpense(this);
        }
    }

    private void addUserExpense(UserExpense userExpense) {
        userExpenseList.add(userExpense);
        if (userExpense.getExpense() == null) {
            userExpense.setExpense(this);
        }
    }
}
