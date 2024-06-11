package com.catbert.tlma.task.handler.v2;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface HarvestHandler {
    boolean handleHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);
    void handleHarvestAction(EntityMaid maid, BlockPos cropPos, BlockState cropState);
}
