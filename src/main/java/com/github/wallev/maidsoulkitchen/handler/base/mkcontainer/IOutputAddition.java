package com.github.wallev.maidsoulkitchen.handler.base.mkcontainer;

import net.minecraft.world.item.ItemStack;

public interface IOutputAddition {

    interface NeedOutputContainer {
        int getOutputContainerSlot();
        int getOutputMealSlot();
        ItemStack getOutputContainerStack();
        ItemStack getOutputMealStack();
        ItemStack getMealContainerItem();
    }

}
