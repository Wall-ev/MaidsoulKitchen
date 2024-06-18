package com.catbert.tlma.task.farm.handler.v1.fruit;

import com.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.teammoeg.caupona.blocks.plants.FruitBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.catbert.tlma.TLMAddon.LOGGER;

/**
 * todo
 */
public class CauponaFruitHandler extends FruitHandler {
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("CauponaFruitHandler handleCanHarvest ");
        return cropState.getBlock() instanceof FruitBlock fruitBlock && fruitBlock.getAge(cropState) >= fruitBlock.getMaxAge();
    }

    @Override
    public boolean canLoad() {
        return Mods.CAUPONA.isLoaded;
    }
}
