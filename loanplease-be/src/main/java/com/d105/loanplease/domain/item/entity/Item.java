package com.d105.loanplease.domain.item.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "item_tb")
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    private String name;
    private String content;
    private Integer price;
    private String type;
}
