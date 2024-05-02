package com.d105.loanplease.domain.store.application.port.in;

import com.d105.loanplease.domain.store.domain.Item;

import java.util.List;

public interface ItemUseCase {

    List<Item> inquiryAllItems();
    Item inquiryItemById(Long id);
}
