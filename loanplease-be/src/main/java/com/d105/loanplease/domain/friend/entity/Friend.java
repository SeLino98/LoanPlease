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

    @ManyToOne(fetch = FetchType.LAZY) //보낸 사람
    @JoinColumn(name ="from_id")
    User fromId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name="to_id") //받는 사람
    Long toId;

    @Column(name = "isAccept")
    boolean isAccept;

}
