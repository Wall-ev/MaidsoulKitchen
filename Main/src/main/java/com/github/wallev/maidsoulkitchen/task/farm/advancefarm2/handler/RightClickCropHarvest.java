package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.clickharvest.ClickHarvestHandler;
import com.github.wallev.maidsoulkitchen.util.fakeplayer.WrappedMaidFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.clickharvest.ClickHarvestHandler.CLICK_BLOCK_HARVESTS;
import static com.github.wallev.maidsoulkitchen.vhelper.IModInfo.LOGGER;

@ICropHarvest.AutoCropHarvestRegister
public class RightClickCropHarvest extends ICropWithSurroundingHarvest {

    public static final ResourceLocation UID = new ResourceLocation("maidsoulkitchen", "right_click_crop_harvest");


    public RightClickCropHarvest() {
        super();
        ClickHarvestHandler.init();
    }

    @Override
    public boolean isFarmCrop(Block block) {
        return CLICK_BLOCK_HARVESTS.contains(block);
    }

    @Override
    public boolean isOverWrite() {
        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (BLACK_LIST.contains(cropState.getBlock()))
            return Result.FAILURE;

        IntegerProperty age = getAge(cropState);
        if (age == null)
            return Result.FAILURE;
        if (!isMature(cropState, age))
            return Result.FAILURE;

        return Result.SUCCESS;
    }

    @Override
    public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        IntegerProperty age = getAge(cropState);
        if (age == null)
            return Result.FAILURE;
        if (!isMature(cropState, age))
            return Result.FAILURE;

        return this.harvestWithoutTool(maid, cropPos, cropState) ? Result.SUCCESS : Result.FAILURE;
    }

    @Override
    public boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos, HarvestData data) {
        if (pathFinding.canPathReach(pos)) {
            data.setWalkAndLookPos(pos);
            return true;
        }

        return super.checkPathReach(maid, pathFinding, pos, data);
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return seed;
    }

    public static boolean harvestWithTool(EntityMaid maid, BlockPos cropPos, BlockState cropState, Predicate<ItemStack> predicate) {
        ItemStack toolStack = ItemsUtil.getStack(maid.getAvailableInv(true), predicate);
        if (!toolStack.isEmpty()) {
            InteractionResult result = WrappedMaidFakePlayer.get(maid).useOnByItem(cropPos, toolStack);
            if (result == InteractionResult.PASS) {
                BLACK_LIST.add(cropState.getBlock());
                LOGGER.warn(BLACK_LIST.toString());
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean harvestWithoutTool(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        InteractionResult result = WrappedMaidFakePlayer.get(maid).useOnByItem(cropPos, Items.AIR.getDefaultInstance());
        if (result == InteractionResult.PASS) {
            BLACK_LIST.add(cropState.getBlock());
            LOGGER.warn(BLACK_LIST.toString());
            return false;
        }
        return true;
    }

    public static boolean hasAgeProperty(BlockState blockState) {
        return getAge(blockState) != null;
    }

    public static boolean isMature(BlockState blockState, IntegerProperty age) {
        return blockState.getOptionalValue(age).orElse(0) >= Collections.max(age.getPossibleValues());
    }

    public static Map<Block, IntegerProperty> AGE_PROPERTIES = new HashMap<>();
    @Nullable
    public static IntegerProperty getAge(BlockState blockState) {
        return (IntegerProperty) blockState.getProperties().stream().filter(property -> property.getName().equals("age")).findFirst().orElse(null);
    }
}
