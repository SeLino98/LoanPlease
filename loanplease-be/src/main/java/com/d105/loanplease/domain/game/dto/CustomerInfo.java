package com.d105.loanplease.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerInfo {
    String name;
    int age;
    String gender;
    String customerImage;
}
