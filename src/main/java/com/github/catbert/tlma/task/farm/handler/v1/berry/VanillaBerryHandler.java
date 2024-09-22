package com.github.catbert.tlma.task.farm.handler.v1.berry;

import com.github.catbert.tlma.TLMAddon;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class VanillaBerryHandler extends BerryHandler{
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "berry_minecraft");

    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("VanillaBerryHandler handleCanHarvest");
        return cropState.getBlock() instanceof SweetBerryBushBlock && cropState.getValue(SweetBerryBushBlock.AGE) >= SweetBerryBushBlock.MAX_AGE;
    }

    @Override
    public boolean canLoad() {
        return true;
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return block instanceof SweetBerryBushBlock;
    }

    @Override
    public ItemStack getIcon() {
        return Items.SWEET_BERRIES.getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
