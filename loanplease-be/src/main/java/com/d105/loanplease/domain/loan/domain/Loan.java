package com.d105.loanplease.domain.loan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "loan_tb")
public class Loan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long loanId;

    private String name;
    private String content;
    private Double interest;
    private Integer period;
    private Integer price;
    private Long limitAmount; // limit이 예약어라 계속 오류남
}
