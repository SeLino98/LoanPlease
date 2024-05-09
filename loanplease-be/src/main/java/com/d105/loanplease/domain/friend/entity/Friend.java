package com.d105.loanplease.domain.friend.entity;

import com.d105.loanplease.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.validation.annotation.Validated;

@Entity
@Builder
@Getter
@Setter
@Validated
@Table(name = "friendship_tb")
public class Friend {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id")
    Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", name = "from_user_id")
    private User fromId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", name = "to_user_id")
    private  User toId;

    @Column(name = "isAccept")
    boolean isAccept;

}
