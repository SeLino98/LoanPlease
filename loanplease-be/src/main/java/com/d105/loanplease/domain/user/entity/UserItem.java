package com.d105.loanplease.domain.user.entity;

import com.d105.loanplease.domain.store.domain.Item;
import com.d105.loanplease.domain.store.domain.Loan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_item_tb")
public class UserItem {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_item_id")
    private Long userItemId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer count;

    public UserItem(Item item, User user) {
        this.user = user;
        this.item = item;
        this.count = 1;
    }

    // 아이템 상품 구매 (기존 아이템이 없는 경우)
    public static UserItem purchaseItem(Item item, User user) {
        UserItem userItem = new UserItem(item, user);

        user.getUserItemList().add(userItem);
        user.purchase(item.getPrice()); // 포인트가 부족한지 체크

        return userItem;
    }

    // 아이템 상품 구매 (기존 아이템이 있는 경우)
    public void purchaseItem(Integer price, User user) {
        user.purchase(price);
        this.count++;
    }
}
