package com.d105.loanplease.domain.store.application.service;


import com.d105.loanplease.domain.store.application.port.in.LoanUseCase;
import com.d105.loanplease.domain.store.application.port.out.LoanPort;
import com.d105.loanplease.domain.store.domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LoanService implements LoanUseCase {

    @Autowired
    private LoanPort loanPort;

    @Override
    public List<Loan> inquiryAllLoans() {
        return loanPort.findAll();
    }

    @Override
    public Loan inquiryLoanById(final Long id) {
        return loanPort.findById(id);
    }
}
