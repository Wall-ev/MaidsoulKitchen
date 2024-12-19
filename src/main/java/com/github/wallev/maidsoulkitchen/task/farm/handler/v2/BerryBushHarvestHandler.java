package com.github.wallev.maidsoulkitchen.task.farm.handler.v2;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import dev.enemeez.simplefarming.common.block.BerryBushBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BerryBushHarvestHandler implements HarvestHandler {
    @Override
    public boolean handleHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return cropState.getBlock() instanceof BerryBushBlock;
    }

    @Override
    public void handleHarvestAction(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        // Handle berry bush harvest action
    }
}
