package com.d105.loanplease.domain.store.application.service;

import com.d105.loanplease.domain.store.application.port.in.ItemUseCase;
import com.d105.loanplease.domain.store.application.port.out.ItemPort;
import com.d105.loanplease.domain.store.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemService implements ItemUseCase {

    @Autowired
    private ItemPort itemPort;


    @Override
    public List<Item> inquiryAllItems() {
        return itemPort.findAll();
    }

    @Override
    public Item inquiryItemById(final Long id) {
        return itemPort.findById(id);
    }
}
