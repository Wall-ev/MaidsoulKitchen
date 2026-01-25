package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

@ICropHarvest.AutoCropHarvestRegister
public class SugarCropHarvest implements ICropHarvest{
    public static final ResourceLocation UID = new ResourceLocation("maidsoulkitchen", "sugar_crop_harvest");

    @Override
    public boolean isFarmCrop(Block block) {
        return block instanceof SugarCaneBlock;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public double getCloseEnoughDist() {
        return 2.0;
    }

    @Override
    public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        BlockState aboveState = maid.level.getBlockState(cropPos.above());
        BlockState belowState = maid.level.getBlockState(cropPos.below(2));
        return cropState.is(Blocks.SUGAR_CANE) && aboveState.is(Blocks.SUGAR_CANE) && canSustainSugarCane(belowState) ? Result.SUCCESS : Result.FAILURE;
    }

    @Override
    public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        maid.destroyBlock(cropPos);
        return Result.SUCCESS;
    }

    @Override
    public boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos, HarvestData data) {
        if (pathFinding.canPathReach(pos)) {
            data.setWalkAndLookPos(pos, pos.above());
            return true;
        }
        return false;
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return stack.getItem() == Items.SUGAR_CANE;
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        Level world = maid.level();
        BlockPos cropPos = basePos.above();
        if (!world.getBlockState(cropPos).canBeReplaced() || world.getBlockState(cropPos).liquid()) {
            return false;
        }
        return canSustainSugarCane(baseState) && hasWaterSourceBlock(maid.level, basePos);
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (seed.getItem() == Items.SUGAR_CANE) {
            maid.placeItemBlock(basePos.above(), seed);
        }
        return seed;
    }

    private boolean canSustainSugarCane(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND);
    }

    private boolean hasWaterSourceBlock(Level world, BlockPos basePos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState offsetState = world.getBlockState(basePos.relative(direction));
            FluidState fluidState = world.getFluidState(basePos.relative(direction));
            if (fluidState.is(FluidTags.WATER) || offsetState.is(Blocks.FROSTED_ICE)) {
                return true;
            }
        }
        return false;
    }
}
