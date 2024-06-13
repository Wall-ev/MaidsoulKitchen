package com.catbert.tlma.api.task.cook;

import com.catbert.tlma.api.ILittleMaidTask;
import com.catbert.tlma.task.ai.brain.MaidCookMakeTask;
import com.catbert.tlma.task.ai.brain.MaidCookMoveTask;
import com.catbert.tlma.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.catbert.tlma.TLMAddon.LOGGER;

public interface ITaskCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends ILittleMaidTask {

    default List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level().isClientSide) return new ArrayList<>();
        LOGGER.info("create brain tasks: " + maid.level() + " " + maid + " " + maid.level().isClientSide);

        MaidRecipesManager<R> cookingPotRecipeMaidRecipesManager = getRecipesManager(maid);
        MaidCookMoveTask<B, R> maidCookMoveTask = new MaidCookMoveTask<>(maid, this, cookingPotRecipeMaidRecipesManager);
        MaidCookMakeTask<B, R> maidCookMakeTask = new MaidCookMakeTask<>(this, cookingPotRecipeMaidRecipesManager);
        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidCookMakeTask));
    }

    default MaidRecipesManager<R> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, getRecipeType(), false);
    }

    @Nullable
    @Override
    default SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FURNACE.get(), 0.5f);
    }

    default double getCloseEnoughDist() {
        return 2.1;
    }

    boolean isCookBE(BlockEntity blockEntity);

    RecipeType<R> getRecipeType();

    boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager);

    void processCookMake(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager);

}
