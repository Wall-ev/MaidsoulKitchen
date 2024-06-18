package com.catbert.tlma.task.ai.brain;

import com.catbert.tlma.api.IAddonMaid;
import com.catbert.tlma.api.task.v1.farm.ICompatFarm;
import com.catbert.tlma.api.task.v1.farm.ICompatFarmHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class MaidCompatFarmMoveTask<T extends ICompatFarmHandler> extends MaidMoveToBlockTask {
    private final ICompatFarm<T> task;
    private final T compatFarmHandler;

    public MaidCompatFarmMoveTask(EntityMaid maid, ICompatFarm<T> task, float movementSpeed) {
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
        ((IAddonMaid)entityMaid).initFakePlayer$tlma();
        BlockState cropState = serverLevel.getBlockState(blockPos);
        return this.task.canHarvest(entityMaid, blockPos, cropState, this.compatFarmHandler);
    }
}
