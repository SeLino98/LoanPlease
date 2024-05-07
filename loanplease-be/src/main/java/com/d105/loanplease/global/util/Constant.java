package com.d105.loanplease.global.util;

public enum Constant {

    SLOT_EXPAND_PRICE(1000);

    private final int price;

    Constant(int price) {
        this.price = price;
    }

    public Integer price() {
        return this.price;
    }
}
