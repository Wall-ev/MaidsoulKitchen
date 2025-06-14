package com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record MaidItem(Item item, int count) {

    public static final MaidItem EMPTY = new MaidItem(ItemStack.EMPTY.getItem(), 0);

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
