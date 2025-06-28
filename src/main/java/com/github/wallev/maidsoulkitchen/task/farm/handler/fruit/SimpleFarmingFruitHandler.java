package com.github.wallev.maidsoulkitchen.task.farm.handler.fruit;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import dev.enemeez.simplefarming.common.block.FruitLeavesBlock;
import dev.enemeez.simplefarming.common.registries.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@TaskClassAnalyzer(TaskInfo.FRUIT_SIMPLE_FARMING)
public class SimpleFarmingFruitHandler extends FruitHandler {
    @Override
    protected boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("SimpleFarmingFruitHandler handleCanHarvest ");
        return cropState.getBlock() instanceof FruitLeavesBlock && cropState.getValue(FruitLeavesBlock.AGE) == FruitLeavesBlock.MAX_AGE;
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
        return FruitHandlerManager.SIMPLE_FARMING.getUid();
    }
}
