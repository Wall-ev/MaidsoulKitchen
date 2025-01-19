package com.github.wallev.maidsoulkitchen.handler.task.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.AbstractMaidCookBe;
import com.github.wallev.maidsoulkitchen.handler.task.AbstractTaskCook;
import com.github.wallev.maidsoulkitchen.handler.task.handler.MaidRecipesManager;
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

@SuppressWarnings("unchecked")
public class MaidCookMakeTask<MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>>
        extends Behavior<EntityMaid> {
    private final AbstractTaskCook<MCB, B, R> task;
    private final MaidRecipesManager<MCB, B, R> maidRecipesManager;
    private final MCB maidCookBe;

    public MaidCookMakeTask(AbstractTaskCook<MCB, B, R> task, MaidRecipesManager<MCB, B, R> maidRecipesManager, MCB maidCookBe) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT));
        this.task = task;
        this.maidRecipesManager = maidRecipesManager;
        this.maidCookBe = maidCookBe;
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
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockPos basePos = posWrapper.currentBlockPosition();
            BlockEntity blockEntity = worldIn.getBlockEntity(basePos);
            if (blockEntity != null && task.isCookBE(blockEntity)) {
                this.maidCookBe.setCookBe((B) blockEntity);
                this.task.processCookMake(worldIn, maidCookBe);
                this.maidRecipesManager.getCookInv().syncInv();
                this.maidRecipesManager.tranOutput2Chest();
                this.maidRecipesManager.getCookInv().syncInv();
            }
            maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
            maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        });
    }
}
