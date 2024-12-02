package com.github.wallev.farmsoulkitchen.task.farm.handler.v1.fruit;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.api.task.v1.farm.ICompatHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CompatFruitHandler extends FruitHandler implements ICompatHandler {
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "fruit_compat");
    @Override
    public boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("CompatFruitHandler handleCanHarvest ");
        return ICompatHandler.super.process(maid, cropPos, cropState);
    }

    @Override
    public boolean canLoad() {
        return true;
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return false;
    }

    @Override
    public ItemStack getIcon() {
        return Items.APPLE.getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
