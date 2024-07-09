package com.github.catbert.tlma.entity.passive;

public enum SideTabIndex {

    SETTING(0),
    BOOK(1),
    INFO(2);

    private final int index;

    SideTabIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
