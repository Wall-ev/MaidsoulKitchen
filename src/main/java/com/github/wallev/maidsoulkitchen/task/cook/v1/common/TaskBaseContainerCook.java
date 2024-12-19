package com.github.wallev.maidsoulkitchen.task.cook.v1.common;

import com.github.wallev.maidsoulkitchen.api.task.v1.cook.IBaseContainerPotCook;
import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TaskBaseContainerCook<B extends BlockEntity, R extends Recipe<? extends Container>> implements ICookTask<B, R>, IBaseContainerPotCook<B, R> {
    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, B blockEntity, MaidRecipesManager<R> recManager) {
        return maidShouldMoveTo(serverLevel, maid, blockEntity, recManager);
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, B blockEntity, MaidRecipesManager<R> recManager) {
        maidCookMake(serverLevel, maid, blockEntity, recManager);
    }
}
