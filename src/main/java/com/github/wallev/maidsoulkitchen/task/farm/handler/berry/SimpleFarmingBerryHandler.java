package com.github.wallev.maidsoulkitchen.task.farm.handler.berry;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import dev.enemeez.simplefarming.common.block.BerryBushBlock;
import dev.enemeez.simplefarming.common.registries.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@TaskClassAnalyzer(TaskInfo.BERRY_SIMPLE_FARMING)
public class SimpleFarmingBerryHandler extends BerryHandler {

    @Override
    protected ActionState processCanHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("SimpleFarmingBerryHandler handleCanHarvest");
        return cropState.getBlock() instanceof BerryBushBlock && cropState.getValue(BerryBushBlock.AGE) >= BerryBushBlock.MAX_AGE ? ActionState.ALLOW : ActionState.DEFAULT;
    }

    @Override
    protected boolean processHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.harvestWithoutTool(maid, cropPos, cropState);
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return block instanceof BerryBushBlock;
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.BLUEBERRY_BUSH.get().getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return BerryHandlerManager.SIMPLE_FARMING.getUid();
    }
}
