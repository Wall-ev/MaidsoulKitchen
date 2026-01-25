package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

@ICropHarvest.AutoCropHarvestRegister
public class CocoaCropHarvest implements ICropHarvest{
    public static final ResourceLocation UID = new ResourceLocation("maidsoulkitchen", "cocoa_crop_harvest");
    private final BoundingBox checkRange = new BoundingBox(-1, 0, -1, 1, 1, 1);

    @Override
    public boolean isFarmCrop(Block block) {
        return block instanceof CocoaBlock;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        return cropState.is(Blocks.COCOA) && cropState.getValue(CocoaBlock.AGE) >= 2 ? Result.SUCCESS : Result.FAILURE;
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
                    if (ICropHarvest.super.checkPathReach(maid, pathFinding, pos.offset(x, y, z), data)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return stack.getItem() == Items.COCOA_BEANS;
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        basePos = basePos.above();
        baseState = maid.level.getBlockState(basePos);
        if (baseState.is(BlockTags.JUNGLE_LOGS) && seed.getItem() == Items.COCOA_BEANS) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState state = maid.level.getBlockState(basePos.relative(direction));
                if (state.canBeReplaced()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        if (seed.getItem() == Items.COCOA_BEANS) {
            basePos = basePos.above();
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos directionPos = basePos.relative(direction);
                if (!seed.isEmpty() && maid.canPlaceBlock(directionPos)) {
                    Level world = maid.level();
                    BlockState cocoaState = Blocks.COCOA.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, direction.getOpposite());
                    world.setBlock(directionPos, cocoaState, Block.UPDATE_ALL_IMMEDIATE);
                    SoundType soundType = cocoaState.getSoundType(world, directionPos, maid);
                    world.playSound(null, directionPos, soundType.getPlaceSound(), SoundSource.BLOCKS,
                            (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                    seed.shrink(1);
                }
            }
        }
        return seed;
    }
}
