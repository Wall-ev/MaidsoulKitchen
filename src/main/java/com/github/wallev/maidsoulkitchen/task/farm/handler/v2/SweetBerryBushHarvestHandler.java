package com.github.wallev.maidsoulkitchen.task.farm.handler.v2;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SweetBerryBushHarvestHandler implements HarvestHandler {
    @Override
    public boolean handleHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return cropState.getBlock() instanceof SweetBerryBushBlock;
    }

    @Override
    public void handleHarvestAction(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        // Handle sweet berry bush harvest action
    }
}
