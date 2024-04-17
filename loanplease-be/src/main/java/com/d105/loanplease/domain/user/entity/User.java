package com.d105.loanplease.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_tb")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String name;
    private String email;
    private Integer score;
    private Integer slot;
    private String profileImg;

    @OneToMany(mappedBy = "user")
    private List<UserItem> userItemList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserLoan> userLoanList = new ArrayList<>();

    @OneToMany(mappedBy = "from")
    private List<Friendship> fromList = new ArrayList<>();

    @OneToMany(mappedBy = "to")
    private List<Friendship> toList = new ArrayList<>();
}