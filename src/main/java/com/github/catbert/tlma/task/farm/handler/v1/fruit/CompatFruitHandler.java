package com.github.catbert.tlma.task.farm.handler.v1.fruit;

import com.github.catbert.tlma.api.task.v1.farm.ICompatHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CompatFruitHandler extends FruitHandler implements ICompatHandler {
    @Override
    public boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("CompatFruitHandler handleCanHarvest ");
        return ICompatHandler.super.process(maid, cropPos, cropState);
    }

    @Override
    public boolean canLoad() {
        return true;
    }
}
