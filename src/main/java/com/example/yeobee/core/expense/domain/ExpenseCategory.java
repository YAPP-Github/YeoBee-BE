package com.example.yeobee.core.expense.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExpenseCategory {
    FOOD("식비"),
    TRANSPORT("교통"),
    LODGE("숙박"),
    TRAVEL("관광"),
    ACTIVITY("액티비티"),
    FLIGHT("항공"),
    SHOPPING("쇼핑"),
    INCOME("예산충전"), // TODO: 추후 네이밍 수정
    ETC("기타");
    private final String name;
}
