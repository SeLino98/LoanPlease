package com.d105.loanplease.domain.user.dto;

import com.d105.loanplease.domain.store.domain.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserItemResDto {

    private Long userItemId;

    private Item item;

    private Integer count;

}
