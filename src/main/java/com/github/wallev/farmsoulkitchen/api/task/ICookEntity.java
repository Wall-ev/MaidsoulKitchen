package com.github.wallev.farmsoulkitchen.api.task;

import net.minecraft.world.item.ItemStack;

public interface ICookEntity {

    /**
     * 获取输出的物品
     * @return ItemStack
     */
    ItemStack getOutputItem();

    /**
     * 是否需要燃料，比如原版熔炉，农夫乐事的烹饪锅
     * @return Boolean
     */
    Boolean needFuel();



}
