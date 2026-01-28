package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler;

import com.github.tartaricacid.touhoulittlemaid.api.task.ISpecialCropHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.crop.SpecialCropManager;
import com.github.tartaricacid.touhoulittlemaid.mixin.accessor.CropBlockAccessor;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager.MaidWorldBlockManager;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.work.MaidDestroyBehavior;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.work.MaidPlaceItemBehavior;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

@ICropHarvest.AutoCropHarvestRegister
public class NormalCropHarvest implements ICropHarvest {
    public static final ResourceLocation UID = VResourceLocation.of(MaidsoulKitchen.MOD_ID, "normal_crop_harvest");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public boolean isFarmCrop(Block block) {
        return block instanceof CropBlock || SpecialCropManager.getBlockCropHandlers().get(block) != null;
    }

    @Override
    public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        Block block = cropState.getBlock();
        // 先判断特殊情况
        ISpecialCropHandler handler = SpecialCropManager.getBlockCropHandlers().get(block);
        if (handler != null) {
            return handler.canHarvest(maid, cropPos, cropState) ? Result.SUCCESS : Result.FAILURE;
        }
        // 其他情况
        return block instanceof CropBlock crop && crop.isMaxAge(cropState) ? Result.SUCCESS : Result.FAILURE;
    }

    @Override
    public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        boolean isDestroyMode = maid.getMainHandItem().getItem() instanceof HoeItem;
        Block cropBlock = cropState.getBlock();

        // 先判断特殊情况
        ISpecialCropHandler handler = SpecialCropManager.getBlockCropHandlers().get(cropBlock);
        if (handler != null) {
            handler.harvest(maid, cropPos, cropState, isDestroyMode);
            return Result.SUCCESS;
        }

        // 其他情况
        if (isDestroyMode) {
            MaidWorldBlockManager.walkAndDestroyBlock(maid, cropPos, maid.getMainHandItem());
            MaidDestroyBehavior.set(maid);
//            maid.destroyBlock(cropPos);
            return Result.NOT_DONE;
        } else if (cropBlock instanceof CropBlockAccessor crop) {
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
        return Result.FAILURE;
    }


    @Override
    public boolean isSeed(ItemStack stack) {
        Item item = stack.getItem();
        // 先判断特殊作物
        ISpecialCropHandler handler = SpecialCropManager.getItemSeedHandlers().get(item);
        if (handler != null) {
            return handler.isSeed(stack);
        }
        // 然后是默认情况
        if (item instanceof ItemNameBlockItem blockNamedItem) {
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable plant) {
                return plant.getPlantType(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) == PlantType.CROP
                        && plant.getPlant(EmptyBlockGetter.INSTANCE, BlockPos.ZERO).getBlock() != Blocks.AIR;
            }
        }
        return false;
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        // 先判断特殊情况
        ISpecialCropHandler handler = SpecialCropManager.getBlockCropHandlers().get(baseState.getBlock());
        if (handler != null) {
            return handler.canPlant(maid, basePos, baseState, seed);
        }

        // 其他情况
        BlockState aboveState = maid.level.getBlockState(basePos.above());
        BlockState belowState = maid.level.getBlockState(basePos.below());
        if (!aboveState.canBeReplaced() || aboveState.liquid()) {
            return false;
        }
        if (seed.getItem() instanceof ItemNameBlockItem blockNamedItem) {
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable plant) {
                return belowState.canSustainPlant(maid.level, basePos, Direction.UP, plant);
            }
        }
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        // 先判断特殊作物
        Item item = seed.getItem();
        ISpecialCropHandler handler = SpecialCropManager.getItemSeedHandlers().get(item);
        if (handler != null) {
            return handler.plant(maid, basePos, baseState, seed);
        }
        // 然后是默认情况
        if (item instanceof ItemNameBlockItem blockNamedItem) {
            Block block = blockNamedItem.getBlock();
            if (block instanceof IPlantable) {
                MaidPlaceItemBehavior.PlaceData.set(maid, new MaidPlaceItemBehavior.PlaceData(basePos, seed));
                MaidPlaceItemBehavior.set(maid);
//                maid.placeItemBlock(basePos, seed);
            }
        }
        return seed;
    }

}
