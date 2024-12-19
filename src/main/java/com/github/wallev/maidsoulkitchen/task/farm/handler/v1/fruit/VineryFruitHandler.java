package com.github.wallev.maidsoulkitchen.task.farm.handler.v1.fruit;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.block.AppleLeaves;
import net.satisfy.vinery.block.CherryLeavesBlock;
import net.satisfy.vinery.registry.ObjectRegistry;

import static net.satisfy.vinery.block.AppleLeaves.HAS_APPLES;
import static net.satisfy.vinery.block.CherryLeavesBlock.HAS_CHERRIES;

public class VineryFruitHandler extends FruitHandler {
    public static final ResourceLocation UID = new ResourceLocation(MaidsoulKitchen.MOD_ID, "fruit_vinery");
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("VineryFruitHandler handleCanHarvest ");
        return (cropState.getBlock() instanceof AppleLeaves && cropState.getValue(AppleLeaves.VARIANT) && cropState.getValue(HAS_APPLES)) ||
                (cropState.getBlock() instanceof CherryLeavesBlock && cropState.getValue(CherryLeavesBlock.VARIANT) && cropState.getValue(HAS_CHERRIES));
    }

    @Override
    public boolean canLoad() {
        return Mods.DV.isLoaded();
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return block instanceof AppleLeaves || block instanceof CherryLeavesBlock;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.CHERRY.get().getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
