package com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;

public record MaidItem(ItemDefinition item, int count) {

    public static final MaidItem EMPTY = new MaidItem(ItemDefinition.EMPTY, 0);

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
