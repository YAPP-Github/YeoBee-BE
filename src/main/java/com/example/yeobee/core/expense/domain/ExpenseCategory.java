package com.example.yeobee.core.expense.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExpenseCategory {
    FOOD("식비"),
    TRANSPORT("교통"),
    LODGE("숙박"),
    TRAVEL("관광"),
    ACTIVITY("액티비티"),
    FLIGHT("항공"),
    SHOPPING("쇼핑"),
    ETC("기타");
    private final String name;
}
