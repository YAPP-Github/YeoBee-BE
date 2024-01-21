package com.example.yeobee.core.expense.dto.request;

import com.example.yeobee.core.expense.domain.ExpensePhoto;

public record ExpenseImage(Long id, String imageUrl) {

    public ExpenseImage(ExpensePhoto expensePhoto) {
        this(expensePhoto.getId(), expensePhoto.getImageUrl());
    }
}
