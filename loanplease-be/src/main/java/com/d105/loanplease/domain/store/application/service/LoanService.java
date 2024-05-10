package com.d105.loanplease.domain.store.application.service;


import com.d105.loanplease.domain.store.application.port.in.LoanUseCase;
import com.d105.loanplease.domain.store.application.port.out.LoanPort;
import com.d105.loanplease.domain.store.domain.Loan;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.entity.UserLoan;
import com.d105.loanplease.domain.user.repository.UserLoanRepository;
import com.d105.loanplease.domain.user.repository.UserRepository;
import com.d105.loanplease.global.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LoanService implements LoanUseCase {

    @Autowired
    private LoanPort loanPort;

    @Autowired
    private UserLoanRepository userLoanRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Loan> inquiryAllLoans() {
        return loanPort.findAll();
    }

    @Override
    public Loan inquiryLoanById(final Long id) {
        return loanPort.findById(id);
    }

    @Override
    public void purchaseLoan(final Long loanId, final Long userId) {

        Loan loan = loanPort.findById(loanId);
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));
        User user = SecurityUtil.getCurrentUserDetails();

        UserLoan userLoan = UserLoan.purchaseLoan(loan, user);

        userLoanRepository.save(userLoan);
    }
}
