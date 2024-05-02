package com.d105.loanplease.domain.loan.application.port.out;

import com.d105.loanplease.domain.loan.domain.Loan;

import java.util.List;

public interface LoanPort {

    List<Loan> findAll();
    Loan findById(Long id);
}
