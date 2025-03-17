package com.github.wallev.verhelper.server.item;

import net.minecraft.world.item.ItemStack;

public class VItemStack {

    private VItemStack() {
    }

    public static ItemStack copyWithCount(ItemStack itemStack, int count) {
        return itemStack.copyWithCount(count);
    }

}
