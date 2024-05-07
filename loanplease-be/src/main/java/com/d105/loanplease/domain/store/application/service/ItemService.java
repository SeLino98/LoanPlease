package com.d105.loanplease.domain.store.application.service;

import com.d105.loanplease.domain.store.application.port.in.ItemUseCase;
import com.d105.loanplease.domain.store.application.port.out.ItemPort;
import com.d105.loanplease.domain.store.domain.Item;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.repository.UserRepository;
import com.d105.loanplease.global.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemService implements ItemUseCase {

    @Autowired
    private ItemPort itemPort;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Item> inquiryAllItems() {
        return itemPort.findAll();
    }

    @Override
    public Item inquiryItemById(final Long itemId) {
        return itemPort.findById(itemId);
    }

    @Override
    public void expandSlot(final Long userId) {
        /**
         * 유저의 슬롯 구매
         * 1. 유저의 슬롯 개수 확인
         * 2. 유저의 보유 포인트 확인
         * 3. 유저의 포인트 차감 + 슬롯 확장
         */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다. "));

        user.expandSlot(Constant.SLOT_EXPAND_PRICE.price());
    }

    @Override
    public void purchaseItem(final Long itemId, final Long userId) {

    }


}
