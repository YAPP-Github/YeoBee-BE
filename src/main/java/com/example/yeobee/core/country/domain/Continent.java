package com.example.yeobee.core.country.domain;

import lombok.Getter;

@Getter
public enum Continent {
    ASIA("아시아"),
    AFRICA("아프리카"),
    NORTH_AMERICA("북아메리카"),
    SOUTH_AMERICA("남아메리카"),
    OCEANIA("오세아니아"),
    EUROPE("유럽");

    private final String name;

    Continent(String name) {
        this.name = name;
    }
}
