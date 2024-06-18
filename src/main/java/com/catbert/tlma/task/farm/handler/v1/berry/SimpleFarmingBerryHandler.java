package com.catbert.tlma.task.farm.handler.v1.berry;

import com.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import dev.enemeez.simplefarming.common.block.BerryBushBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.catbert.tlma.TLMAddon.LOGGER;

public class SimpleFarmingBerryHandler extends BerryHandler{
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("SimpleFarmingBerryHandler handleCanHarvest");
        return cropState.getBlock() instanceof BerryBushBlock && cropState.getValue(BerryBushBlock.AGE) >= BerryBushBlock.MAX_AGE;
    }

    @Override
    public boolean canLoad() {
        return Mods.SF.isLoaded;
    }
}
