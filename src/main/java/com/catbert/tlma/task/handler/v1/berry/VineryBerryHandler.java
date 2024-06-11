package com.catbert.tlma.task.handler.v1.berry;

import com.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import satisfyu.vinery.block.grape.GrapeBush;
import satisfyu.vinery.block.grape.GrapeVineBlock;

import static com.catbert.tlma.TLMAddon.LOGGER;

public class VineryBerryHandler extends BerryHandler{
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        LOGGER.info("VineryBerryHandler handleCanHarvest");
        Block block = cropState.getBlock();
        return (block instanceof GrapeBush || block instanceof GrapeVineBlock) && cropState.getValue(GrapeBush.AGE) >= 3;
    }

    @Override
    public boolean canLoad() {
        return Mods.DV.isLoaded;
    }
}
