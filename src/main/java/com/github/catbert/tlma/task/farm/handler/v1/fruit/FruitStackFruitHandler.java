package com.github.catbert.tlma.task.farm.handler.v1.fruit;

import com.fruitstack.fruitstack.common.registry.ModItems;
import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.fruitstack.fruitstack.common.block.BlockFruitCrop;
import com.fruitstack.fruitstack.common.block.GrapeCropBlock;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FruitStackFruitHandler extends FruitHandler{
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "fruit_fruit_stack");
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("FruitStackFruitHandler handleCanHarvest ");

        return (cropState.getBlock() instanceof BlockFruitCrop blockFruitCrop && blockFruitCrop.isMaxAge(cropState)) ||
                (cropState.getBlock() instanceof GrapeCropBlock grapeCropBlock && cropState.getValue(GrapeCropBlock.AGE) >= 7);
    }

    @Override
    public boolean canLoad() {
        return Mods.FS.isLoaded;
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return block instanceof BlockFruitCrop || block instanceof GrapeCropBlock;
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.LITCHI_BLOCK.get().getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
