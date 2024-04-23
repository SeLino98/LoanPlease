package com.d105.loanplease.domain.user.repository;

import com.d105.loanplease.domain.user.entity.UserLoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoanRepository extends JpaRepository<UserLoan, Long> {
}
