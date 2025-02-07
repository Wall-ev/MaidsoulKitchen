package com.github.wallev.maidsoulkitchen.task.farm.handler.v1.berry.farmersrespite;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import umpaz.farmersrespite.common.block.TeaBushBlock;
import umpaz.farmersrespite.common.registry.FRItems;

public class FarmersRespiteYellowTeaBerryHandler extends FarmersRespiteTeaBerryHandler {
    public static final ResourceLocation UID = new ResourceLocation(MaidsoulKitchen.MOD_ID, "berry_farmersrespite_yellowtea");

    @Override
    protected ActionState processCanHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (this.hasTool(maid)) {
            if (cropState.getBlock() instanceof TeaBushBlock && cropState.getValue(TeaBushBlock.AGE) >= 1) {
                return ActionState.ALLOW;
            } else {
                return ActionState.DEFAULT;
            }
        } else {
            return ActionState.DENY;
        }
    }

    @Override
    public ItemStack getIcon() {
        return FRItems.YELLOW_TEA_LEAVES.get().getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
