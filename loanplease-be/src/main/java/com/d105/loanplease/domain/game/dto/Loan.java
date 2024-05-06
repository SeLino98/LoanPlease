package com.d105.loanplease.domain.game.dto;

import lombok.Data;

@Data
public class Loan {
    double interest;
    int period;
    String purpose;
    String loanImage;
}
