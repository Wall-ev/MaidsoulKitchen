package com.catbert.tlma.task.farm.handler.v1.fruit;

import com.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import satisfyu.vinery.block.AppleLeaves;
import satisfyu.vinery.block.CherryLeaves;

import static com.catbert.tlma.TLMAddon.LOGGER;
import static satisfyu.vinery.block.AppleLeaves.HAS_APPLES;
import static satisfyu.vinery.block.CherryLeaves.HAS_CHERRIES;

public class VineryFruitHandler extends FruitHandler {
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        LOGGER.info("VineryFruitHandler handleCanHarvest ");
        return (cropState.getBlock() instanceof AppleLeaves && cropState.getValue(AppleLeaves.VARIANT) && cropState.getValue(HAS_APPLES)) ||
                (cropState.getBlock() instanceof CherryLeaves && cropState.getValue(CherryLeaves.VARIANT) && cropState.getValue(HAS_CHERRIES));
    }

    @Override
    public boolean canLoad() {
        return Mods.DV.isLoaded;
    }
}
