package com.github.wallev.maidsoulkitchen.task.farm.handler.berry;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;

@TaskClassAnalyzer(TaskInfo.BERRY_MINECRAFT)
public class VanillaBerryHandler extends BerryHandler{
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(MaidsoulKitchen.MOD_ID, "berry_minecraft");

    @Override
    protected ActionState processCanHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
//        LOGGER.info("VanillaBerryHandler handleCanHarvest");
        return cropState.getBlock() instanceof SweetBerryBushBlock && cropState.getValue(SweetBerryBushBlock.AGE) >= SweetBerryBushBlock.MAX_AGE ? ActionState.ALLOW : ActionState.DEFAULT;
    }

    @Override
    protected boolean processHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return this.harvestWithoutTool(maid, cropPos, cropState);
    }

    @Override
    public boolean canLoad() {
        return TaskInfo.BERRY_MINECRAFT.canLoad();
    }

    @Override
    public boolean isFarmBlock(Block block) {
        return block instanceof SweetBerryBushBlock;
    }

    @Override
    public ItemStack getIcon() {
        return Items.SWEET_BERRIES.getDefaultInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
