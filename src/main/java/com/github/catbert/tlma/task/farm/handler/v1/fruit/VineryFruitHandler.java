package com.github.catbert.tlma.task.farm.handler.v1.fruit;

import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.block.AppleLeaves;
import net.satisfy.vinery.block.CherryLeavesBlock;

import static net.satisfy.vinery.block.AppleLeaves.HAS_APPLES;
import static net.satisfy.vinery.block.CherryLeavesBlock.HAS_CHERRIES;

public class VineryFruitHandler extends FruitHandler {
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("VineryFruitHandler handleCanHarvest ");
        return (cropState.getBlock() instanceof AppleLeaves && cropState.getValue(AppleLeaves.VARIANT) && cropState.getValue(HAS_APPLES)) ||
                (cropState.getBlock() instanceof CherryLeavesBlock && cropState.getValue(CherryLeavesBlock.VARIANT) && cropState.getValue(HAS_CHERRIES));
    }

    @Override
    public boolean canLoad() {
        return Mods.DV.isLoaded;
    }
}
