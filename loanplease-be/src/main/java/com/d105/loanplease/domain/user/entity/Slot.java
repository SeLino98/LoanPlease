package com.d105.loanplease.domain.user.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "slot_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Slot {

    @Id @GeneratedValue
    @Column(name = "slot_id")
    private Long slotId;

    private Integer slot_1;
    private Integer slot_2;
    private Integer slot_3;
    private Integer slot_4;
    private Integer slot_5;

    @OneToOne(mappedBy = "slot")
    private User user;

    public static Slot makeSlot(User user) {
        Slot slot = new Slot();

        slot.slot_1 = 1;
        slot.slot_2 = 2;
        slot.slot_3 = 3;

        slot.user = user;
        user.setSlot(slot);
        return slot;
    }
}
