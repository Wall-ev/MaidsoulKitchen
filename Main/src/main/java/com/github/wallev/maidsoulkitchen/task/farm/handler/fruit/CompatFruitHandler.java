package com.github.wallev.maidsoulkitchen.task.farm.handler.fruit;

import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@TaskClassAnalyzer(TaskInfo.FRUIT_COMPAT)
public class CompatFruitHandler extends FruitHandler implements ICompatHandler {
    @Override
    public boolean process(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("CompatFruitHandler handleCanHarvest ");
        return ICompatHandler.super.process(maid, cropPos, cropState);
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
        return FruitHandlerManager.COMPAT.getUid();
    }
}
