package com.d105.loanplease.domain.store.application.service;


import com.d105.loanplease.domain.store.application.port.in.LoanUseCase;
import com.d105.loanplease.domain.store.application.port.out.LoanPort;
import com.d105.loanplease.domain.store.application.service.response.ChooseLoanResponse;
import com.d105.loanplease.domain.store.application.service.response.PurchaseLoanResponse;
import com.d105.loanplease.domain.store.domain.Loan;
import com.d105.loanplease.domain.user.entity.Slot;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.entity.UserLoan;
import com.d105.loanplease.domain.user.repository.UserLoanRepository;
import com.d105.loanplease.domain.user.repository.UserRepository;
import com.d105.loanplease.global.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class LoanService implements LoanUseCase {
    private final SecurityUtil securityUtil;
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
    @Transactional
    public ResponseEntity<PurchaseLoanResponse> purchaseLoan(final Long loanId) {

        Loan loan = loanPort.findById(loanId);
        User user = securityUtil.getCurrentUserDetails();

        UserLoan userLoan = UserLoan.purchaseLoan(loan, user);
        userLoanRepository.save(userLoan);

        PurchaseLoanResponse response = new PurchaseLoanResponse(user.getPoint(), user.getUserLoanList());
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<ChooseLoanResponse> changeSlot(final Integer slot_1,
                                                         final Integer slot_2,
                                                         final Integer slot_3,
                                                         final Integer slot_4,
                                                         final Integer slot_5) {

        User user = securityUtil.getCurrentUserDetails();
        Slot slot = user.getSlot();

        slot.changeSlot(slot_1, slot_2, slot_3, slot_4, slot_5);
        ChooseLoanResponse response = new ChooseLoanResponse(user.getSlotNum(), slot);

        return ResponseEntity.ok(response);
    }
}
