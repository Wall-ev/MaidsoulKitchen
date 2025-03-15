package com.github.wallev.maidsoulkitchen.handler;

import net.minecraft.world.item.ItemStack;

public class VItemStackHelper {

    public static ItemStack copyWithCount(ItemStack itemStack, int count) {
        return itemStack.copyWithCount(count);
    }

}
