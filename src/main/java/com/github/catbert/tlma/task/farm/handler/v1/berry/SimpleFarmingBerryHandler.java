package com.github.catbert.tlma.task.farm.handler.v1.berry;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import dev.enemeez.simplefarming.common.block.BerryBushBlock;
import dev.enemeez.simplefarming.common.registries.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleFarmingBerryHandler extends BerryHandler{
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "simplefarming");

    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("SimpleFarmingBerryHandler handleCanHarvest");
        return cropState.getBlock() instanceof BerryBushBlock && cropState.getValue(BerryBushBlock.AGE) >= BerryBushBlock.MAX_AGE;
    }

    @Override
    public boolean canLoad() {
        return Mods.SF.isLoaded;
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.BLUEBERRY_PIE.get().getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
