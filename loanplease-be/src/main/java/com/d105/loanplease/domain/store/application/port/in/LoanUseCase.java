package com.d105.loanplease.domain.store.application.port.in;

import com.d105.loanplease.domain.store.application.service.response.ChooseLoanResponse;
import com.d105.loanplease.domain.store.domain.Loan;
import com.d105.loanplease.domain.user.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoanUseCase {

    List<Loan> inquiryAllLoans();
    Loan inquiryLoanById(Long id);
    void purchaseLoan(Long loanId, Long userId);
    ResponseEntity<ChooseLoanResponse> changeSlot(Integer slot_1, Integer slot_2, Integer slot_3, Integer slot_4, Integer slot_5);
}
