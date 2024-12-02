package com.github.wallev.farmsoulkitchen.task.farm.handler.v2.fruit;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface ICompatFarmHandler {

    default boolean shouldMoveTo(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.canHarvest(maid, cropPos, cropState);
    }

    boolean canLoad();

    boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    default void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {

    }

}
