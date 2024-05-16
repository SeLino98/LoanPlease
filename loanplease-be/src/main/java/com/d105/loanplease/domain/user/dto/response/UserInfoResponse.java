package com.d105.loanplease.domain.user.dto.response;

import com.d105.loanplease.domain.user.dto.UserItemResDto;
import com.d105.loanplease.domain.user.dto.UserLoanResDto;
import com.d105.loanplease.domain.user.entity.UserItem;
import com.d105.loanplease.domain.user.entity.UserLoan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {


    private String nickname;
    private String email;
    private String profileImage;
    private Integer point;

    // Slot, UserLoan, UserItem 등 Entity는 꼭!!! Dto로 변환하고 response에 담아 보내자
    private Integer slotNum;
    private Integer slot_1;
    private Integer slot_2;
    private Integer slot_3;
    private Integer slot_4;
    private Integer slot_5;


    private List<UserLoanResDto> userLoanList;
    private List<UserItemResDto> userItemList;

}
