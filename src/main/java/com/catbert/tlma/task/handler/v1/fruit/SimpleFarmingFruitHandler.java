package com.catbert.tlma.task.handler.v1.fruit;

import com.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import dev.enemeez.simplefarming.common.block.FruitLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.catbert.tlma.TLMAddon.LOGGER;

public class SimpleFarmingFruitHandler extends FruitHandler {
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        LOGGER.info("SimpleFarmingFruitHandler handleCanHarvest ");
        return cropState.getBlock() instanceof FruitLeavesBlock && cropState.getValue(FruitLeavesBlock.AGE) == FruitLeavesBlock.MAX_AGE;
    }

    @Override
    public boolean canLoad() {
        return Mods.SF.isLoaded;
    }
}
