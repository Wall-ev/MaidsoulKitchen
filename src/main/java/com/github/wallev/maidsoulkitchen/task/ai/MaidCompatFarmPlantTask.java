package com.github.wallev.maidsoulkitchen.task.ai;

import com.github.wallev.maidsoulkitchen.api.task.v1.farm.ICompatFarm;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.IHandlerInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class MaidCompatFarmPlantTask<T extends ICompatFarmHandler & IHandlerInfo> extends Behavior<EntityMaid> {

    private final ICompatFarm<T, ?> task;
    private final T compatFarmHandler;

    public MaidCompatFarmPlantTask(EntityMaid maid, ICompatFarm<T, ?> task) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT));
        this.task = task;
        this.compatFarmHandler = task.getCompatHandler(maid);
    }

    public MaidCompatFarmPlantTask(EntityMaid maid, ICompatFarm<T, ?> task, T handler) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT));
        this.task = task;
        this.compatFarmHandler = handler;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        Brain<EntityMaid> brain = owner.getBrain();
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            Vec3 targetV3d = targetPos.currentPosition();
            if (owner.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2)) {
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
    protected void start(ServerLevel world, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockPos basePos = posWrapper.currentBlockPosition();
            BlockState cropState = world.getBlockState(basePos);
            if (maid.canDestroyBlock(basePos) && task.canHarvest(maid, basePos, cropState, this.compatFarmHandler)) {
                task.harvest(maid, basePos, cropState, this.compatFarmHandler);
                maid.swing(InteractionHand.MAIN_HAND);
                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            }
        });
    }

}
