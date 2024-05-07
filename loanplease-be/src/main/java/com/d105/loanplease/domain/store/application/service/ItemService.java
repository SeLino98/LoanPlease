package com.d105.loanplease.domain.store.application.service;

import com.d105.loanplease.domain.store.application.port.in.ItemUseCase;
import com.d105.loanplease.domain.store.application.port.out.ItemPort;
import com.d105.loanplease.domain.store.domain.Item;
import com.d105.loanplease.domain.user.entity.User;
import com.d105.loanplease.domain.user.entity.UserItem;
import com.d105.loanplease.domain.user.repository.UserItemRepository;
import com.d105.loanplease.domain.user.repository.UserRepository;
import com.d105.loanplease.global.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ItemService implements ItemUseCase {

    @Autowired
    private ItemPort itemPort;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserItemRepository userItemRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));

        user.expandSlot(Constant.SLOT_EXPAND_PRICE.price());
    }

    @Override
    public void purchaseItem(final Long itemId, final Long userId) {
        Item item = itemPort.findById(itemId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));

        Long userItemId = user.hasItemHistory(itemId);

        if(userItemId==null) { // 해당 아이템에 대한 구매 내역이 없을 때 -> DB에 새로 추가
            UserItem userItem = UserItem.purchaseItem(item, user);
            userItemRepository.save(userItem);
        } else { // 해당 아이템을 구매한 내역이 있을 경우 -> 구매 내역을 가져와 아이템 개수 +1
            UserItem userItem = userItemRepository.findById(userItemId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 보유 아이템은 없는 아이템입니다."));
            userItem.purchaseItem(item.getPrice(), user);
        }

    }


}
