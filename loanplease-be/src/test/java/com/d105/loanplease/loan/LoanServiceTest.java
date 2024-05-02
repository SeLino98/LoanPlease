package com.d105.loanplease.loan;

import com.d105.loanplease.domain.loan.application.service.LoanService;
import com.d105.loanplease.domain.loan.domain.Loan;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class LoanServiceTest {

    @Autowired
    private LoanService loanService;

    @Test
    void 대출상품전체조회() {
        List<Loan> loanList = loanService.inquiryAllLoans();
        assertThat(loanList.size()).isEqualTo(0);
    }

    @Test
    void 특정대출상품조회() {
//        final Long loanId = 1L;
//        Loan loan = loanService.inquiryLoan(loanId);
    }

    @Test
    void 대출상품구매() {

    }
}
