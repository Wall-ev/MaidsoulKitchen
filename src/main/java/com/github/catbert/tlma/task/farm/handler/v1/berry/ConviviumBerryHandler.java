package com.github.catbert.tlma.task.farm.handler.v1.berry;

import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.khjxiaogu.convivium.blocks.camellia.CamelliaFlowerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * todo
 */
public class ConviviumBerryHandler extends BerryHandler{
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return cropState.getBlock() instanceof CamelliaFlowerBlock camelliaFlowerBlock && camelliaFlowerBlock.getAge(cropState) >= camelliaFlowerBlock.getMaxAge();
    }

    @Override
    public boolean canLoad() {
        return Mods.CONVIVIUM.isLoaded;
    }
}
