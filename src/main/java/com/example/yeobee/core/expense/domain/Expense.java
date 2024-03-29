package com.example.yeobee.core.expense.domain;

import com.example.yeobee.core.expense.dto.request.ExpenseCreateRequestDto;
import com.example.yeobee.core.expense.dto.request.ExpenseUpdateRequestDto;
import com.example.yeobee.core.trip.domain.Trip;
import com.example.yeobee.core.trip.domain.TripCurrency;
import com.example.yeobee.core.trip.domain.TripUser;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private LocalDateTime payedAt;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;

    @Enumerated(EnumType.STRING)
    private ExpenseMethod expenseMethod;

    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;

    @Enumerated(EnumType.STRING)
    private CalculationType calculationType = CalculationType.NONE;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "trip_currency_id")
    private TripCurrency tripCurrency;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payer_id")
    private TripUser payer;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpensePhoto> expensePhotoList = new ArrayList<>();

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserExpense> userExpenseList = new ArrayList<>();

    public Expense(ExpenseCreateRequestDto request) {
        amount = request.amount();
        name = request.name();
        payedAt = request.payedAt();
        expenseCategory = request.expenseCategory();
        expenseMethod = request.expenseMethod();
        expenseType = request.expenseType();
        calculationType = request.calculationType();
        request.imageList().forEach((e) -> expensePhotoList.add(new ExpensePhoto(e, this)));
    }

    public void update(ExpenseUpdateRequestDto request) {
        clear();
        amount = request.amount();
        name = request.name();
        expenseCategory = request.expenseCategory();
        expenseMethod = request.expenseMethod();
        expenseType = request.expenseType();
        calculationType = request.calculationType();
        request.imageList().forEach((e) -> expensePhotoList.add(new ExpensePhoto(e, this)));
    }

    public void addUserExpense(UserExpense userExpense) {
        userExpenseList.add(userExpense);
    }

    private void clear() {
        expensePhotoList.forEach((e) -> e.setExpense(null));
        userExpenseList.forEach((e) -> e.setExpense(null));
        expensePhotoList.clear();
        userExpenseList.clear();
    }

    public Long getPayerId() {
        return (payer == null) ? null : payer.getId();
    }

    public Long getPayerUserId() {
        return (payer == null) ? null : payer.getUserId();
    }

    public String getPayerName() {
        return (payer == null) ? "공동경비" : payer.getName();
    }

    public Long getKoreanAmount() {
        return tripCurrency.getExchangeRate().getKoreanAmount(amount);
    }

    public String getCurrencyCode() {
        return tripCurrency.getCurrency().getCode();
    }
}
