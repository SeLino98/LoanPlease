package com.d105.loanplease.domain.loan.application.port.in;

import com.d105.loanplease.domain.loan.domain.Loan;

import java.util.List;

public interface LoanUseCase {

    List<Loan> inquiryAllLoans();
    Loan inquiryLoanById(Long id);
}
