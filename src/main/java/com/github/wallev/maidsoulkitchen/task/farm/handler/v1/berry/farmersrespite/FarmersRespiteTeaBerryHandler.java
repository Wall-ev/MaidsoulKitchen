package com.github.wallev.maidsoulkitchen.task.farm.handler.v1.berry.farmersrespite;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.wallev.maidsoulkitchen.task.farm.handler.v1.berry.BerryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import umpaz.farmersrespite.common.block.TeaBushBlock;

public abstract class FarmersRespiteTeaBerryHandler extends BerryHandler {

    protected boolean hasTool(EntityMaid maid) {
        return ItemsUtil.findStackSlot(maid.getAvailableInv(true), itemStack -> itemStack.is(Tags.Items.SHEARS)) > -1;
    }

    @Override
    protected boolean processHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.harvestWithTool(maid, cropPos, cropState, itemStack -> itemStack.is(Tags.Items.SHEARS));
    }

    @Override
    public boolean canLoad() {
        return Mods.FRD.isLoaded();
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return block instanceof TeaBushBlock;
    }


}
