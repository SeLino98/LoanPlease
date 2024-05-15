package com.d105.loanplease.domain.user.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Table(name = "slot_tb")
@Getter
@Builder
@AllArgsConstructor
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
    @JsonManagedReference
    private User user;

    public static Slot makeSlot(User user) {
        Slot slot = Slot.builder().slot_1(1).slot_2(2).slot_3(3).slot_4(0).slot_5(0).build();
        slot.user = user;
        user.setSlot(slot);
        return slot;
    }
}
