package com.github.wallev.farmsoulkitchen.api.task.v2;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public interface ICookBaseBe<B extends BlockEntity, R extends Recipe<? extends Container>> {

    /**
     * 判断厨具是否在烹任
     * @param be 厨具
     * @param recipe 厨具内容器对应的配方，可谓空
     * @param level Level
     * @return 厨具内的物品是否可以烹饪
     */
    boolean innerCanCraft(B be, @Nullable R recipe, Level level);

}
