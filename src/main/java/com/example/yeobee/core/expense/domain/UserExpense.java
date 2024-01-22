package com.example.yeobee.core.expense.domain;

import com.example.yeobee.core.expense.dto.request.Payer;
import com.example.yeobee.core.trip.domain.TripUser;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_user_id")
    private TripUser tripUser;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    public UserExpense(Payer payer) {
        amount = payer.amount();
    }

    @Override
    public String toString() {
        return "UserExpense{" +
               "id=" + id +
               ", amount=" + amount +
               ", tripUser=" + tripUser +
               ", expense=" + expense +
               '}';
    }
}
