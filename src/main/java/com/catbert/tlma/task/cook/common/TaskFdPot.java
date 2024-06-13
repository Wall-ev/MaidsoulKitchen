package com.catbert.tlma.task.cook.common;

import com.catbert.tlma.api.task.cook.IFDPotCook;
import com.catbert.tlma.api.task.cook.ITaskCook;
import com.catbert.tlma.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TaskFdPot<B extends BlockEntity, R extends Recipe<? extends Container>> implements ITaskCook<B, R>, IFDPotCook<B, R> {
    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        return maidShouldMoveTo(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        maidCookMake(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }
}
