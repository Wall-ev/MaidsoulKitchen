package com.catbert.tlma.task.handler.v1.fruit;

import com.catbert.tlma.foundation.utility.Mods;
import com.fruitstack.fruitstack.common.block.BlockFruitCrop;
import com.fruitstack.fruitstack.common.block.GrapeCropBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.catbert.tlma.TLMAddon.LOGGER;

public class FruitStackFruitHandler extends FruitHandler{
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        LOGGER.info("FruitStackFruitHandler handleCanHarvest ");

        return (cropState.getBlock() instanceof BlockFruitCrop blockFruitCrop && blockFruitCrop.isMaxAge(cropState)) ||
                (cropState.getBlock() instanceof GrapeCropBlock grapeCropBlock && cropState.getValue(GrapeCropBlock.AGE) >= 7);
    }

    @Override
    public boolean canLoad() {
        return Mods.FS.isLoaded;
    }
}
