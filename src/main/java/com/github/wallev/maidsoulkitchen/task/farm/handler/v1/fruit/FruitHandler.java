package com.github.wallev.maidsoulkitchen.task.farm.handler.v1.fruit;

import com.github.wallev.maidsoulkitchen.api.task.v1.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.v1.farm.IHandlerInfo;
import com.github.wallev.maidsoulkitchen.task.farm.FarmType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FruitHandler implements ICompatFarmHandler, IHandlerInfo {
    // 下一级处理者
    private FruitHandler nextHandler;

    @Override
    public void setNextHandler(ICompatFarmHandler nextHandler) {
        this.nextHandler = (FruitHandler) nextHandler;
    }

    @Override
    public boolean shouldMoveTo(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.canHarvest(maid, cropPos, cropState);
    }

    @Override
    public FarmType getFarmType() {
        return FarmType.FRUIT;
    }

    protected abstract boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    public final boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (!process(maid, cropPos, cropState)) {
            return nextHandler != null && nextHandler.canHarvest(maid, cropPos, cropState);
        }else {
            return true;
        }
    }
}
