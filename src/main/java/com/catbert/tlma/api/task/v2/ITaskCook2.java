package com.catbert.tlma.api.task.v2;

import com.catbert.tlma.api.task.v1.cook.ITaskCook;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ITaskCook2<B extends BlockEntity, R extends Recipe<? extends Container>> extends ITaskCook<B, R>{
}
