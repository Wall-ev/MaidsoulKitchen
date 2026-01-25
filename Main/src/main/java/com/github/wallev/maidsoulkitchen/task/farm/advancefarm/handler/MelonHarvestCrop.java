//package com.github.wallev.maidsoulkitchen.task.farm.advancefarm.handler;
//
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.wallev.maidsoulkitchen.event.MelonConfigEvent;
//import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai.CropResult;
//import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
//import com.github.wallev.maidsoulkitchen.vhelper.server.item.VEnchantmentHelper;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.BaseFireBlock;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.LevelEvent;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.gameevent.GameEvent;
//import net.minecraft.world.level.material.FluidState;
//
//@IHarvestCrop.AutoCropHarvestRegister
//public class MelonHarvestCrop extends IHarvestCrop {
//    public final ResourceLocation UID = VResourceLocation.ofMod("melon_harvest_crop");
//
//    public MelonHarvestCrop() {
//        MelonConfigEvent.handleConfig();
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return UID;
//    }
//
//    @Override
//    public boolean isFarmCrop(Block block) {
//        return MelonConfigEvent.MELON_STEM_MAP.containsKey(block);
//    }
//
//    @Override
//    public boolean isSeed(ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult) {
//        Block block = cropState.getBlock();
//        Block stemBlock = MelonConfigEvent.MELON_STEM_MAP.get(block);
//        if (stemBlock != null) {
//            for (Direction direction : Direction.Plane.HORIZONTAL) {
//                BlockState offsetState = maid.level.getBlockState(cropPos.relative(direction));
//                if (offsetState.is(stemBlock)) {
//                    return Result.SUCCESS;
//                }
//            }
//        }
//        return Result.FAIL;
//    }
//
//    @Override
//    public Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult) {
//        Block block = cropState.getBlock();
//        Block stemBlock = MelonConfigEvent.MELON_STEM_MAP.get(block);
//        if (stemBlock != null) {
//            ItemStack mainHandItem = maid.getMainHandItem();
//            if (VEnchantmentHelper.hasSilkTouch(mainHandItem)) {
//                if (this.destroyBlockByHandItem(maid, cropPos)) {
//                    mainHandItem.hurtAndBreak(1, maid, (e) -> {
//                        e.broadcastBreakEvent(InteractionHand.MAIN_HAND);
//                    });
//                }
//            } else {
//                maid.destroyBlock(cropPos);
//            }
//            return Result.SUCCESS;
//        } else {
//            return Result.FAIL;
//        }
//    }
//
//    @Override
//    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, CropResult cropResult) {
//        return false;
//    }
//
//    @Override
//    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, CropResult cropResult) {
//        return null;
//    }
//
//    public boolean destroyBlockByHandItem(EntityMaid maid, BlockPos pos) {
//        return this.destroyBlockByHandItem(maid, pos, true);
//    }
//
//    public boolean destroyBlockByHandItem(EntityMaid maid, BlockPos pos, boolean dropBlock) {
//        return maid.canDestroyBlock(pos) && this.destroyBlockByHandItem(maid, maid.level, pos, dropBlock);
//    }
//
//    private boolean destroyBlockByHandItem(EntityMaid maid, Level level, BlockPos blockPos, boolean dropBlock) {
//        BlockState blockState = level.getBlockState(blockPos);
//        if (blockState.isAir()) {
//            return false;
//        } else {
//            FluidState fluidState = level.getFluidState(blockPos);
//            if (!(blockState.getBlock() instanceof BaseFireBlock)) {
//                level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
//            }
//            if (dropBlock) {
//                BlockEntity blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(blockPos) : null;
//                maid.dropResourcesToMaidInv(blockState, level, blockPos, blockEntity, maid, maid.getMainHandItem());
//            }
//            boolean setResult = level.setBlock(blockPos, fluidState.createLegacyBlock(), Block.UPDATE_ALL);
//            if (setResult) {
//                level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(maid, blockState));
//            }
//            return setResult;
//        }
//    }
//}
