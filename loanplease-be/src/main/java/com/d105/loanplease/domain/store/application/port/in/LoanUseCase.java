package com.d105.loanplease.domain.store.application.port.in;

import com.d105.loanplease.domain.store.domain.Loan;
import com.d105.loanplease.domain.user.entity.User;

import java.util.List;

public interface LoanUseCase {

    List<Loan> inquiryAllLoans();
    Loan inquiryLoanById(Long id);
    void purchaseLoan(Long id, User user);
}
