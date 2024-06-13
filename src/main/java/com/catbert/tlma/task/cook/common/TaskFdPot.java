package com.catbert.tlma.task.cook.common;

import com.catbert.tlma.api.task.cook.IFDPotCook;
import com.catbert.tlma.api.task.cook.ITaskCook;
import com.catbert.tlma.task.ai.brain.MaidCookMakeTask;
import com.catbert.tlma.task.ai.brain.MaidCookMoveTask;
import com.catbert.tlma.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

import static com.catbert.tlma.TLMAddon.LOGGER;

public abstract class TaskFdPot<B extends BlockEntity, R extends Recipe<? extends Container>> implements ITaskCook<B, R>, IFDPotCook<B, R> {
    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        return maidShouldMoveTo(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        maidCookMake(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level().isClientSide) return new ArrayList<>();
        LOGGER.info("create brain tasks: " + maid.level() + " " + maid + " " + maid.level().isClientSide);

        MaidRecipesManager<R> cookingPotRecipeMaidRecipesManager = getRecipesManager(maid);
        MaidCookMoveTask<B, R> maidCookMoveTask = new MaidCookMoveTask<>(maid, this, cookingPotRecipeMaidRecipesManager);
        MaidCookMakeTask<B, R> maidCookMakeTask = new MaidCookMakeTask<>(this, cookingPotRecipeMaidRecipesManager);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidCookMakeTask));
    }

    public MaidRecipesManager<R> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, getRecipeType(), false);
    }

}
