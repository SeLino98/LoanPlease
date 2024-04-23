package com.d105.loanplease.domain.loan.repository;

import com.d105.loanplease.domain.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
