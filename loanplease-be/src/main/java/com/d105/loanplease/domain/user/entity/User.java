package com.d105.loanplease.domain.user.entity;

import com.d105.loanplease.domain.store.domain.Slot;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Setter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@Table(name = "user_tb")
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String nickname;
    private String email;
    private String profileImg;
    private Integer score;
    private Integer slotNum;
    private Integer point;
    private String role;

    @OneToMany(mappedBy = "user")
    private List<UserItem> userItemList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserLoan> userLoanList = new ArrayList<>();

    @OneToMany(mappedBy = "from")
    private List<Friendship> fromList = new ArrayList<>();

    @OneToMany(mappedBy = "to")
    private List<Friendship> toList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    // 유저가 해당 대출 상품을 가지고 있는지 확인
    public void hasLoan(Long loanId) {
        for(UserLoan userLoan: userLoanList) {
            Assert.isTrue(userLoan.getLoan().getLoanId()!=loanId, "해당 대출 상품을 이미 가지고 있습니다.");
        }
    }

    // 유저가 해당 아이템을 산 적이 있는지 확인
    public Long hasItemHistory(Long itemId) {
        for(UserItem userItem: userItemList) {
            if(userItem.getItem().getItemId()==itemId) return userItem.getUserItemId();
        }
        return null;
    }

    // 상점에서 무언가를 구매
    public void purchase(Integer price) {
        Assert.isTrue(point >= price, "구매할 포인트가 부족합니다.");
        this.point -= price;
    }

    // 유저 슬롯 확장
    public void expandSlot(Integer price) {
        Assert.isTrue(this.slotNum < 5, "슬롯 확장을 더 이상 할 수 없습니다.");
        this.purchase(price);
        this.slotNum++;
    }
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", score=" + score +
                ", slot=" + slot +
                ", profileImg='" + profileImg + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public void setPoint(int score){ this.point = point; }
}