package com.github.catbert.tlma.entity.passive;

public enum SideTab {

    TASK_SETTING(0),
    TASK_BOOK(1),
    TASK_INFO(2),
    MOD_SETTING(3);

    private final int index;

    SideTab(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
