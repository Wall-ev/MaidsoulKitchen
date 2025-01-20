package com.github.wallev.maidsoulkitchen.handler.base.mkcontainer;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ICookBeAccessor<B extends BlockEntity, R extends Recipe<? extends Container>> {

    /**
     * 判断厨具内部的原料是否可以烹饪
     * <br>即有符合配方的原料
     * <br>但不会检测额外条件
     * <br>比如：需要燃料，加水等
     *
     * @return 是否可以烹饪
     */
    boolean canCook$msk();

}
