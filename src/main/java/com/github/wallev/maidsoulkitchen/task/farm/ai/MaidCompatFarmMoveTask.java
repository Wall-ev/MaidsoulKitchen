package com.github.wallev.maidsoulkitchen.task.farm.ai;

import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmTask;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatHandlerInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class MaidCompatFarmMoveTask<T extends ICompatFarmHandler & ICompatHandlerInfo> extends MaidMoveToBlockTask implements VBehaviorControl {
    private final ICompatFarmTask<T> task;
    private final T compatFarmHandler;

    public MaidCompatFarmMoveTask(EntityMaid maid, ICompatFarmTask<T> task, float movementSpeed) {
        super(movementSpeed, 2);
        this.task = task;
        this.compatFarmHandler = task.getCompatHandler(maid);
    }

    public T getCompatFarmHandler() {
        return compatFarmHandler;
    }

    @Override
    protected void start(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        this.searchForDestination(pLevel, pEntity);
    }

    @Override
    protected boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, BlockPos blockPos) {
        BlockState cropState = serverLevel.getBlockState(blockPos);
        return this.task.canHarvest(entityMaid, blockPos, cropState, this.compatFarmHandler);
    }
}
