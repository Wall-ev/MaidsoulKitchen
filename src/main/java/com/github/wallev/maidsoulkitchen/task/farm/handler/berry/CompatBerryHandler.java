package com.github.wallev.maidsoulkitchen.task.farm.handler.berry;

import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@TaskClassAnalyzer(TaskInfo.BERRY_COMPAT)
public class CompatBerryHandler extends BerryHandler implements ICompatHandler {

    @Override
    public ActionState processCanHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("CompatBerryHandler handleCanHarvest ");
        return ICompatHandler.super.process(maid, cropPos, cropState) ? ActionState.ALLOW : ActionState.DEFAULT;
    }

    @Override
    protected boolean processHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.harvestWithoutTool(maid, cropPos, cropState);
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return false;
    }

    @Override
    public ItemStack getIcon() {
        return Items.SWEET_BERRIES.getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return BerryHandlerManager.MINECRAFT.getUid();
    }
}
