package com.d105.loanplease.domain.game.Fields;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OccypType {
    LABORERS("Laborers", "일용직"),
    MANAGERS("Managers", "CEO"),
    SALESSTAFF("Sales staff", "영업"),
    HIGHSKILL("High skill tech staff", "고급 기술자"),
    CORESTAFF("Core staff", "부장/과장"),
    DRIVERS("Drivers", "운전 기사"),
    MEDICINESTAFF("Medicine staff", "의료계"),
    ACCOUNTANTS("Accountants", "회계사"),
    REALITY("Realty agents", "부동산 중개인"),
    SECURITY("Security staff", "보안 직원"),
    CLEANING("Cleaning staff", "청소원"),
    PRIVATE("Private service staff", "가정부"),
    COOKING("Cooking staff", "요식업"),
    SECRETARIES("Secretaries", "비서"),
    HR("HR staff", "인사팀"),
    IT("IT staff", "IT 개발자"),
    LOWSKILL("Low-skill Laborers", "아르바이트"),
    WAITERS("Waiters/barmen staff", "웨이터");


    private final String englishName;
    private final String koreanName;
}
