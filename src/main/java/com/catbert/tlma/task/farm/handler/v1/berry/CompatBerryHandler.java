package com.catbert.tlma.task.farm.handler.v1.berry;

import com.catbert.tlma.api.task.v1.farm.ICompatHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.catbert.tlma.TLMAddon.LOGGER;

public class CompatBerryHandler extends BerryHandler implements ICompatHandler {
    @Override
    public boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        LOGGER.info("CompatBerryHandler handleCanHarvest ");
        return ICompatHandler.super.process(maid, cropPos, cropState);
    }

    @Override
    public boolean canLoad() {
        return true;
    }
}
