package com.example.yeobee.core.expense.dto;

import com.example.yeobee.core.expense.domain.ExpensePhoto;

public record ExpensePhotoDto(Long id, String imageUrl) {

    public ExpensePhotoDto(ExpensePhoto expensePhoto) {
        this(expensePhoto.getId(), expensePhoto.getImageUrl());
    }
}
