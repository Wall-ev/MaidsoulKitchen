package com.github.catbert.tlma.task.farm.handler.v1.berry;

import com.github.catbert.tlma.api.task.v1.farm.ICompatFarmHandler;
import com.github.catbert.tlma.api.task.v1.farm.IHandlerInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public abstract class BerryHandler implements ICompatFarmHandler, IHandlerInfo {
    private static final Set<BerryHandler> berryHandlers = new HashSet<>();

    // 下一级处理者
    private BerryHandler nextHandler;

    // todo
    protected BerryHandler() {
        berryHandlers.add(this);
    }

    public static Set<BerryHandler> getBerryHandlers() {
        return berryHandlers;
    }

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
