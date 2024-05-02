package com.d105.loanplease.domain.loan.application.service;


import com.d105.loanplease.domain.loan.application.port.in.LoanUseCase;
import com.d105.loanplease.domain.loan.application.port.out.LoanPort;
import com.d105.loanplease.domain.loan.domain.Loan;
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
}
