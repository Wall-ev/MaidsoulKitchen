package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;

@ICropHarvest.AutoCropHarvestRegister
public class CactusCropHarvest extends ICropWithSurroundingHarvest{
    public static final ResourceLocation UID = new ResourceLocation("maidsoulkitchen", "cactus_crop_harvest");
    @Override
    public boolean isFarmCrop(Block block) {
        return block instanceof CactusBlock;
    }

    @Override
    public boolean hasAviShape(ServerLevel worldIn, EntityMaid maid, BlockPos cropPos) {
        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        BlockState aboveState = maid.level.getBlockState(cropPos.above());
        BlockState belowState = maid.level.getBlockState(cropPos.below());
        return cropState.is(Blocks.CACTUS) && aboveState.is(Blocks.CACTUS) && canSustainCactus(belowState) ? Result.SUCCESS : Result.FAILURE;

    }

    @Override
    public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        maid.destroyBlock(cropPos);
        return Result.SUCCESS;
    }

    @Override
    public double getCloseEnoughDist() {
        return 2.5;
    }

    @Override
    public boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos, HarvestData data) {
        for (int x = checkRange.minX(); x <= checkRange.maxX(); x++) {
            for (int y = checkRange.minY(); y <= checkRange.maxY(); y++) {
                for (int z = checkRange.minZ(); z <= checkRange.maxZ(); z++) {
                    if (pathFinding.canPathReach(pos.offset(x, y, z))) {
                        data.setWalkAndLookPos(pos.offset(x, y, z), pos.above());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return stack.getItem() == Items.CACTUS;
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
//        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(Block.byItem(seed.getItem()));
//        Block block = ForgeRegistries.BLOCKS.getValue(key);
//        return block.canSurvive(baseState, maid.level, basePos);

        Level world = maid.level;
        if (!world.getBlockState(basePos).canBeReplaced() || world.getBlockState(basePos).liquid()) {
            return false;
        }

        BlockState blockstate1 = world.getBlockState(basePos.below());
        return this.canSustainCactus(blockstate1) && hasEnouhtgArea(world, basePos);
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (seed.getItem() == Items.CACTUS) {
            maid.placeItemBlock(basePos.below(), seed);
        }
        return seed;
    }

    private boolean hasEnouhtgArea(Level world, BlockPos basePos) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState blockstate = world.getBlockState(basePos.relative(direction));
            if (blockstate.isSolid() || world.getFluidState(basePos.relative(direction)).is(FluidTags.LAVA)) {
                return false;
            }
        }

        return true;
    }

    private boolean canSustainCactus(BlockState state) {
        return state.is(BlockTags.SAND);
    }
}
