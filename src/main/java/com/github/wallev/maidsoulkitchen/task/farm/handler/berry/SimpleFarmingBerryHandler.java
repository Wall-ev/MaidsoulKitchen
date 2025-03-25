package com.github.wallev.maidsoulkitchen.task.farm.handler.berry;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleFarmingBerryHandler extends BerryHandler{
    public static final ResourceLocation UID = new ResourceLocation(MaidsoulKitchen.MOD_ID, "berry_simple_farming");

    @Override
    protected ActionState processCanHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("SimpleFarmingBerryHandler handleCanHarvest");
        return ActionState.DEFAULT;
    }

    @Override
    protected boolean processHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.harvestWithoutTool(maid, cropPos, cropState);
    }

    @Override
    public boolean canLoad() {
        return Mods.SF.isLoaded();
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return false;
    }

    @Override
    public ItemStack getIcon() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
