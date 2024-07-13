package com.github.catbert.tlma.task.farm.handler.v1.berry;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.block.grape.GrapeBush;
import net.satisfy.vinery.block.grape.GrapeVineBlock;
import net.satisfy.vinery.registry.ObjectRegistry;

public class VineryBerryHandler extends BerryHandler{
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "minecraft");

    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("VineryBerryHandler handleCanHarvest");
        Block block = cropState.getBlock();
        return (block instanceof GrapeBush || block instanceof GrapeVineBlock) && cropState.getValue(GrapeBush.AGE) >= 3;
    }

    @Override
    public boolean canLoad() {
        return Mods.DV.isLoaded;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.RED_GRAPE_BUSH.get().asItem().getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
