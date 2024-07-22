package com.github.catbert.tlma.task.farm.handler.v2.fruit;

import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.task.farm.handler.v1.fruit.FruitHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import dev.enemeez.simplefarming.common.block.FruitLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleFarmingFruitHandler implements ICompatFarmHandler {

    @Override
    public boolean canLoad() {
        return Mods.SF.isLoaded;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return cropState.getBlock() instanceof FruitLeavesBlock && cropState.getValue(FruitLeavesBlock.AGE) == FruitLeavesBlock.MAX_AGE;
    }
}
