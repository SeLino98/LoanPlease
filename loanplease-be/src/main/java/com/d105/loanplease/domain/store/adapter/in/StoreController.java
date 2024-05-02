package com.d105.loanplease.domain.store.adapter.in;

import com.d105.loanplease.domain.store.application.port.in.LoanUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private LoanUseCase loanUseCase;
}
