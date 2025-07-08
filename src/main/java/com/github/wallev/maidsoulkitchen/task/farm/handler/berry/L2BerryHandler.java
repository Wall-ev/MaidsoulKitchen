package com.github.wallev.maidsoulkitchen.task.farm.handler.berry;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.util.InvUtil;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static com.github.wallev.maidsoulkitchen.task.TaskInfo.BERRY_L2_HARVESTER;

@TaskClassAnalyzer(BERRY_L2_HARVESTER)
public class L2BerryHandler extends BerryHandler {
    @Override
    protected Result processCanHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (cropState.getBlock() instanceof HarvestableBlock harvestableBlock) {
            return harvestableBlock.getHarvestResult(maid.level(), cropState, cropPos) != null ?
                    Result.ALLOW : Result.DENY;
        }
        return Result.DEFAULT;
    }

    @Override
    protected boolean processHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (cropState.getBlock() instanceof HarvestableBlock harvestableBlock) {
            HarvestResult result = harvestableBlock.getHarvestResult(maid.level(), cropState, cropPos);
            if (result != null) {
                result.updateState(maid.level(), cropPos);
                InvUtil.insertAndPop(maid, result.drops());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return block instanceof HarvestableBlock;
    }

    @Override
    public ItemStack getIcon() {
        return Items.SWEET_BERRIES.getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return BERRY_L2_HARVESTER.getUid();
    }
}
