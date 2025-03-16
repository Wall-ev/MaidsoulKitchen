package com.github.wallev.maidsoulkitchen.task.farm.handler.v1.fruit;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleFarmingFruitHandler extends FruitHandler {
    public static final ResourceLocation UID = new ResourceLocation(MaidsoulKitchen.MOD_ID, "fruit_simple_farming");
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("SimpleFarmingFruitHandler handleCanHarvest ");
        return false;
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
