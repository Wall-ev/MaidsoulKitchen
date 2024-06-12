package com.catbert.tlma.task.ai.brain;

import com.catbert.tlma.api.task.cook.ITaskCook;
import com.catbert.tlma.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MaidCookMakeTask<T extends Recipe<? extends Container>, C extends BlockEntity> extends Behavior<EntityMaid> {
    private final ITaskCook<T, C> task;
    private final MaidRecipesManager<T> maidRecipesManager;

    public MaidCookMakeTask(ITaskCook<T, C> task, @Nullable MaidRecipesManager<T> maidRecipesManager) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT));
        this.task = task;
        this.maidRecipesManager = maidRecipesManager;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        Brain<EntityMaid> brain = maid.getBrain();
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            Vec3 targetV3d = targetPos.currentPosition();
            if (maid.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2)) {
                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
                if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
                    brain.eraseMemory(InitEntities.TARGET_POS.get());
                }
                return false;
            }
            return true;
        }).orElse(false);
//        return (Boolean)brain.getMemory((MemoryModuleType)InitEntities.TARGET_POS.get()).map((targetPos) -> {
//            Vec3 targetV3d = ((BlockPosTracker) targetPos).currentPosition();
//            if (!(maid.distanceToSqr(targetV3d) > Math.pow(1.0, 2.0))) {
//                return true;
//            } else {
//                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
//                if (walkTarget.isEmpty() || !((WalkTarget)walkTarget.get()).getTarget().currentPosition().equals(targetV3d)) {
//                    brain.eraseMemory((MemoryModuleType)InitEntities.TARGET_POS.get());
//                }
//
//                return false;
//            }
//        }).orElse(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockPos basePos = posWrapper.currentBlockPosition();
            BlockEntity blockEntity = worldIn.getBlockEntity(basePos);
            if (blockEntity != null) {
                task.processCookMake(worldIn, maid, (C) blockEntity, this.maidRecipesManager);
            }
            maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        });
    }
}
