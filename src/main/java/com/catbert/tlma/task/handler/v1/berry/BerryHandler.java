package com.catbert.tlma.task.handler.v1.berry;

import com.catbert.tlma.api.ICompatFarmHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BerryHandler implements ICompatFarmHandler {
    // 下一级处理者
    private BerryHandler nextHandler;

    @Override
    public void setNextHandler(ICompatFarmHandler nextHandler) {
        this.nextHandler = (BerryHandler) nextHandler;
    }

    @Override
    public boolean shouldMoveTo(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.canHarvest(maid, cropPos, cropState);
    }

    protected abstract boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    public final boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (!process(maid, cropPos, cropState)) {
            return nextHandler != null && nextHandler.canHarvest(maid, cropPos, cropState);
        } else {
            return true;
        }
    }
}
