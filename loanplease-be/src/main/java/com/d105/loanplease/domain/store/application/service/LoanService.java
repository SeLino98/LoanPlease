package com.d105.loanplease.domain.store.application.service;


import com.d105.loanplease.domain.store.application.port.in.LoanUseCase;
import com.d105.loanplease.domain.store.application.port.out.LoanPort;
import com.d105.loanplease.domain.store.domain.Loan;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.entity.UserLoan;
import com.d105.loanplease.domain.user.repository.UserLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LoanService implements LoanUseCase {

    @Autowired
    private LoanPort loanPort;

    @Autowired
    private UserLoanRepository userLoanRepository;

    @Override
    public List<Loan> inquiryAllLoans() {
        return loanPort.findAll();
    }

    @Override
    public Loan inquiryLoanById(final Long id) {
        return loanPort.findById(id);
    }

    @Override
    public void purchaseLoan(final Long id, final User user) {
        Loan loan = loanPort.findById(id);
        UserLoan userLoan = new UserLoan(loan, user);

        userLoanRepository.save(userLoan);
    }
}
