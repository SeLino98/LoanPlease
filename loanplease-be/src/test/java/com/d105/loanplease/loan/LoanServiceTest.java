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
        assertThat(loanList.size()).isEqualTo(2);
    }

    @Test
    void 특정대출상품조회() {
        final Long firstLoanId = 1L;
        Loan firstLoan = loanService.inquiryLoanById(firstLoanId);

        assertThat(firstLoan.getContent()).isEqualTo("이 상품은 첫번째 테스트용 대출 상품입니다");
        assertThat(firstLoan.getInterest()).isEqualTo(0.1);

        final Long secondLoanId = 2L;
        Loan secondLoan = loanService.inquiryLoanById(secondLoanId);

        assertThat(secondLoan.getContent()).isEqualTo("이 상품은 두번째 테스트용 대출 상품입니다");
        assertThat(secondLoan.getInterest()).isEqualTo(0.2);
    }

    @Test
    void 대출상품구매() {

    }
}
