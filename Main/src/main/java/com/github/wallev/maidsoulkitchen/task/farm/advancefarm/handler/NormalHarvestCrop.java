package com.github.wallev.maidsoulkitchen.task.farm.advancefarm.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.mixin.accessor.CropBlockAccessor;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai.CropResult;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

@IHarvestCrop.AutoCropHarvestRegister
public class NormalHarvestCrop extends IHarvestCrop {
    public final ResourceLocation UID = VResourceLocation.ofMod("normal_harvest_crop");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public boolean isFarmCrop(Block block) {
        return block instanceof CropBlock crop;
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return false;
    }

    @Override
    public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult) {
        if (!(cropState.getBlock() instanceof CropBlock crop))
            return Result.FAIL;
        return crop.isMaxAge(cropState) ? Result.SUCCESS : Result.PASS;
    }


    @Override
    public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult) {
        if (!(cropState.getBlock() instanceof CropBlock cropBlock))
            return Result.FAIL;
        if (cropBlock instanceof CropBlockAccessor crop) {
            BlockEntity blockEntity = cropState.hasBlockEntity() ? maid.level.getBlockEntity(cropPos) : null;
            maid.dropResourcesToMaidInv(cropState, maid.level, cropPos, blockEntity, maid, maid.getMainHandItem());
            maid.level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, cropPos, Block.getId(cropState));
            // 直接设置 Age 为 0
            if (cropState.hasProperty(crop.tlmAgeProperty())) {
                try {
                    cropState = cropState.trySetValue(crop.tlmAgeProperty(), 0);
                } catch (IllegalArgumentException ignore) {
                }
            }
            maid.level.setBlock(cropPos, cropState, Block.UPDATE_ALL);
            maid.level.gameEvent(maid, GameEvent.BLOCK_CHANGE, cropPos);
            return Result.SUCCESS;
        }
        return Result.PASS;
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, CropResult cropResult) {
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, CropResult cropResult) {
        return null;
    }
}
