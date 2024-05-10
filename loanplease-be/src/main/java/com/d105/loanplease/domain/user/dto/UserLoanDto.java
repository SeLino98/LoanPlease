package com.d105.loanplease.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoanDto {

    private String loanName;
    private String content;
    private Double interest;
    private Integer period;
    private Long limitAmount;
    private String img;
}
