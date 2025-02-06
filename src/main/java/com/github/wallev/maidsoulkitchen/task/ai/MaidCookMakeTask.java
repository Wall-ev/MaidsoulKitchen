package com.github.wallev.maidsoulkitchen.task.ai;

import com.github.wallev.maidsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
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

import java.util.Optional;

public class MaidCookMakeTask<B extends BlockEntity, R extends Recipe<? extends Container>> extends Behavior<EntityMaid> {
    private final ICookTask<B, R> task;
    private final MaidRecipesManager<R> maidRecipesManager;

    public MaidCookMakeTask(ICookTask<B, R> task,MaidRecipesManager<R> maidRecipesManager) {
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
                    brain.eraseMemory(MkEntities.WORK_POS.get());
                }
                return false;
            }
            return true;
        }).orElse(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        if (maid != this.maidRecipesManager.getMaid()) {
            return;
        }
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockPos basePos = posWrapper.currentBlockPosition();
            BlockEntity blockEntity = worldIn.getBlockEntity(basePos);
            if (blockEntity != null && task.isCookBE(blockEntity)) {
                this.task.processCookMake(worldIn, maid, (B) blockEntity, this.maidRecipesManager);
                this.maidRecipesManager.getCookInv().syncInv();
                this.maidRecipesManager.tranOutput2Chest();
                this.maidRecipesManager.getCookInv().syncInv();
            }
            maid.getBrain().eraseMemory(MkEntities.WORK_POS.get());
            maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        });
    }
}
