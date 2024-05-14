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

@Builder
@Getter
public class UserInfoResponse {


    private String nickname;
    private String email;
    private String profileImage;

    // Slot, UserLoan, UserItem 등 Entity는 꼭!!! Dto로 변환하고 response에 담아 보내자
    private Integer slotNum;
    private Slot slot;
    private List<UserLoan> userLoanList = new ArrayList<>();
    private List<UserItem> userItemList = new ArrayList<>();

//    public UserInfoResponse(String nickname,String email, String profileImage,final Integer slotNum, final Slot slot) {
//
//    }


    public UserInfoResponse(String nickname, String email, String profileImage, Integer slotNum, Slot slot, List<UserLoan> userLoanList, List<UserItem> userItemList) {
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.slotNum = slotNum;
        this.slot = slot;
        this.userLoanList = userLoanList;
        this.userItemList = userItemList;
    }

//    public void addItem(final UserItem userItem) {
//        userItemList.add(userItem);
//    }
//
//    public void addLoan(final UserLoan userLoan) {
//        userLoanList.add(userLoan);
//    }
}
