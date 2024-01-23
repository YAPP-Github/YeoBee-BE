package com.example.yeobee.core.expense.domain;

import com.example.yeobee.common.entity.BaseEntity;
import com.example.yeobee.core.expense.dto.common.ExpensePhotoDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpensePhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    public ExpensePhoto(ExpensePhotoDto expenseImage) {
        imageUrl = expenseImage.imageUrl();
    }
}
