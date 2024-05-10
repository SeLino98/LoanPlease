package com.d105.loanplease.domain.user.dto.response;

import com.d105.loanplease.domain.store.domain.Item;
import com.d105.loanplease.domain.store.domain.Loan;
import com.d105.loanplease.domain.user.dto.UserItemDto;
import com.d105.loanplease.domain.user.dto.UserLoanDto;
import com.d105.loanplease.domain.user.entity.Slot;
import com.d105.loanplease.domain.user.entity.UserItem;
import com.d105.loanplease.domain.user.entity.UserLoan;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserInfoResponse {

    // Slot, UserLoan, UserItem 등 Entity는 꼭!!! Dto로 변환하고 response에 담아 보내자
    private Integer slotNum;
    private Slot slot;
    private List<UserLoan> userLoanList = new ArrayList<>();
    private List<UserItem> userItemList = new ArrayList<>();

    public UserInfoResponse(final Integer slotNum, final Slot slot) {

    }

    public void addItem(final UserItem userItem) {
        userItemList.add(userItem);
    }

    public void addLoan(final UserLoan userLoan) {
        userLoanList.add(userLoan);
    }
}
