package com.github.catbert.tlma.task.farm.handler.v1.fruit;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import dev.enemeez.simplefarming.common.block.FruitLeavesBlock;
import dev.enemeez.simplefarming.common.registries.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleFarmingFruitHandler extends FruitHandler {
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "fruit_simple_farming");
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("SimpleFarmingFruitHandler handleCanHarvest ");
        return cropState.getBlock() instanceof FruitLeavesBlock && cropState.getValue(FruitLeavesBlock.AGE) == FruitLeavesBlock.MAX_AGE;
    }

    @Override
    public boolean canLoad() {
        return Mods.SF.isLoaded;
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return block instanceof FruitLeavesBlock;
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.CHERRIES.get().getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
