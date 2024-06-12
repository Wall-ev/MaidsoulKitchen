package com.catbert.tlma.task.farm.handler.v1.berry;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;

import static com.catbert.tlma.TLMAddon.LOGGER;

public class VanillaBerryHandler extends BerryHandler{
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        LOGGER.info("VanillaBerryHandler handleCanHarvest");
        return cropState.getBlock() instanceof SweetBerryBushBlock && cropState.getValue(SweetBerryBushBlock.AGE) >= SweetBerryBushBlock.MAX_AGE;
    }

    @Override
    public boolean canLoad() {
        return true;
    }
}
