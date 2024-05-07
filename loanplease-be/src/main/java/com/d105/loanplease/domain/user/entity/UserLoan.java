package com.d105.loanplease.domain.user.entity;

import com.d105.loanplease.domain.loan.entity.Loan;
import com.d105.loanplease.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_loan_tb")
public class UserLoan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_loan_id")
    private Long userLoanId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
}
