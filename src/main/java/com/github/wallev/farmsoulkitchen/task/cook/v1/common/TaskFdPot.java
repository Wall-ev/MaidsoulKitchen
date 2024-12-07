package com.github.wallev.farmsoulkitchen.task.cook.v1.common;

import com.github.wallev.farmsoulkitchen.api.task.v1.cook.IFdPotCook;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.farmsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TaskFdPot<B extends BlockEntity, R extends Recipe<? extends Container>> implements ICookTask<B, R>, IFdPotCook<B, R> {
    @Override
    public boolean shouldMoveTo(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> manager) {
        return maidShouldMoveTo(level, maid, be, manager);
    }

    @Override
    public void processCookMake(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> manager) {
        maidCookMake(level, maid, be, manager);
    }
}
