package com.github.wallev.maidsoulkitchen.handler;

import net.minecraft.world.item.ItemStack;

public class VItemStackHelper {

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
