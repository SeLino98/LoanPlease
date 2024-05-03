package com.d105.loanplease.domain.store.adapter.in;

import com.d105.loanplease.domain.store.application.port.in.ItemUseCase;
import com.d105.loanplease.domain.store.application.port.in.LoanUseCase;
import com.d105.loanplease.domain.store.application.service.response.InquiryStoreResponse;
import com.d105.loanplease.domain.store.domain.Item;
import com.d105.loanplease.domain.store.domain.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private LoanUseCase loanUseCase;

    @Autowired
    private ItemUseCase itemUseCase;

    @PostMapping("/choose-loan")
    public void chooseLoan() {}

    @GetMapping("/items")
    public ResponseEntity<InquiryStoreResponse> inquiryStore() {
        List<Loan> loans = loanUseCase.inquiryAllLoans();
        List<Item> items = itemUseCase.inquiryAllItems();

        InquiryStoreResponse response = new InquiryStoreResponse();

        for(Loan loan: loans) {
            response.addLoan(loan);
        }

        for(Item item: items) {
            response.addItem(item);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/items/slot")
    public void purchaseSlot() {}

    @PostMapping("/items/loan")
    public void purchaseLoan() {}

    @PostMapping("/items/oneoff")
    public void purchaseItem() {}
}
