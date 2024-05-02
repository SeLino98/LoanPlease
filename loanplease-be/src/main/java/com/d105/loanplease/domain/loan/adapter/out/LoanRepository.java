package com.d105.loanplease.domain.loan.adapter.out;

import com.d105.loanplease.domain.loan.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
