package com.github.wallev.maidsoulkitchen.task.farm.handler.berry.farmersrespite;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.BerryHandlerManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import umpaz.farmersrespite.common.block.TeaBushBlock;
import umpaz.farmersrespite.common.registry.FRItems;

@TaskClassAnalyzer(TaskInfo.BERRY_FARMERS_RESPITE_YELLOW_TEA)
public class FarmersRespiteYellowTeaBerryHandler extends FarmersRespiteTeaBerryHandler {

    @Override
    protected Result processCanHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (this.hasTool(maid)) {
            if (cropState.getBlock() instanceof TeaBushBlock && cropState.getValue(TeaBushBlock.AGE) >= 1) {
                return Result.ALLOW;
            } else {
                return Result.DEFAULT;
            }
        } else {
            return Result.DENY;
        }
    }

    @Override
    public ItemStack getIcon() {
        return FRItems.YELLOW_TEA_LEAVES.get().getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return BerryHandlerManager.FARMERS_RESPITE_YELLOW_TEA.getUid();
    }
}
