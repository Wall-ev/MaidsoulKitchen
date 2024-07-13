package com.github.catbert.tlma.task.farm.handler.v1.berry;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.task.v1.farm.ICompatHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class CompatBerryHandler extends BerryHandler implements ICompatHandler {
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "compat");

    @Override
    public boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("CompatBerryHandler handleCanHarvest ");
        return ICompatHandler.super.process(maid, cropPos, cropState);
    }

    @Override
    public boolean canLoad() {
        return true;
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
