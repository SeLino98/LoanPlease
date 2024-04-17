package com.d105.loanplease.domain.user.entity;

import com.d105.loanplease.domain.item.entity.Item;
import jakarta.persistence.*;

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
}
