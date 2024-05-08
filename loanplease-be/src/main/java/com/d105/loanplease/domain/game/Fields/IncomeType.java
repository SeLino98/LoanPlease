package com.d105.loanplease.domain.game.Fields;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IncomeType {
    COMMERCIAL("Commercial associate", "상인"),
    WORKING("Working", "바나나"),
    STATE("State servant", "공무원"),
    PENISIONER("Pensioner", "연금 수령"),
    STUDENT("Student", "학생");

    // 영문명을 반환하는 메서드
    private final String englishName;
    // 한글명을 반환하는 메서드
    private final String koreanName;

}
