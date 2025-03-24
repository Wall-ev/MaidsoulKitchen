package com.github.wallev.verhelper.server.item;

import net.minecraft.world.item.ItemStack;

public class VItemStack {

    public static ItemStack copyWithCount(ItemStack itemStack, int count) {
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack itemstack = itemStack.copy();
            itemstack.setCount(count);
            return itemstack;
        }
    }

}
