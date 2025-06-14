//package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight;
//
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
//import com.github.wallev.maidsoulkitchen.init.MkEntities;
//import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager;
//import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
//import com.github.wallev.verhelper.server.ai.VBehaviorControl;
//import com.google.common.collect.ImmutableMap;
//import net.minecraft.core.BlockPos;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.ai.Brain;
//import net.minecraft.world.entity.ai.behavior.Behavior;
//import net.minecraft.world.entity.ai.behavior.PositionTracker;
//import net.minecraft.world.entity.ai.memory.MemoryStatus;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.Vec3;
//import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
//import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
//
//import java.util.Optional;
//
//public class MaidCuttingMakeTask extends Behavior<EntityMaid> implements VBehaviorControl {
//    private final TaskFdCuttingBoard task;
//    private final MaidRecipesManager<CuttingBoardRecipe> maidRecipesManager;
//    private boolean maidHand = false;
//    private int tick = 0;
//    private Item processItem = null;
//
//    public MaidCuttingMakeTask(TaskFdCuttingBoard task, MaidRecipesManager<CuttingBoardRecipe> maidRecipesManager) {
//        super(ImmutableMap.of(MkEntities.WORK_POS.get(), MemoryStatus.VALUE_PRESENT), 1200);
//        this.task = task;
//        this.maidRecipesManager = maidRecipesManager;
//    }
//
//    @Override
//    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
//        Brain<EntityMaid> brain = maid.getBrain();
//        return brain.getMemory(MkEntities.WORK_POS.get()).map(targetPos -> {
//            Vec3 targetV3d = targetPos.currentPosition();
//            if (maid.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2)) {
////                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
////                if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
//////                    brain.eraseMemory(InitEntities.TARGET_POS.get());
////                }
//                return false;
//            }
//            return true;
//        }).orElse(false);
//    }
//
//    @Override
//    protected boolean canStillUse(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
//        return maid.getBrain().hasMemoryValue(MkEntities.WORK_POS.get())
//                && (!maid.getMainHandItem().isEmpty() && this.processItem != null &&
//                (maid.getOffhandItem().is(this.processItem) || isProcessItem(worldIn, maid)));
//    }
//
//    private boolean isProcessItem(ServerLevel worldIn, EntityMaid maid) {
//        Optional<PositionTracker> tracker = MemoryUtil.getWorkPos(maid);
//
//        if (tracker.isPresent()) {
//            BlockEntity blockEntity = worldIn.getBlockEntity(tracker.get().currentBlockPosition());
//            if (blockEntity instanceof CuttingBoardBlockEntity cuttingBoardBlockEntity) {
//                return cuttingBoardBlockEntity.getStoredItem().is(this.processItem);
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
//        super.start(worldIn, maid, pGameTime);
//        MemoryUtil.getWorkPos(maid).ifPresent(posWrapper -> {
//            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
//            if (blockEntity instanceof CuttingBoardBlockEntity cuttingBoardBlockEntity) {
////                debugInfo(maid, posWrapper.currentBlockPosition());
//                task.processCookMake(worldIn, maid, cuttingBoardBlockEntity, this.maidRecipesManager, (item) -> {
//                    this.processItem = item;
//                });
//                this.maidRecipesManager.syncInv();
//            }
//        });
//    }
//
//    private void debugInfo(EntityMaid maid, BlockPos pos) {
//        BlockState blockState = maid.level.getBlockState(pos);
//        MaidsoulKitchen.LOGGER.debug("{} CookMake {} {}", maid, pos, blockState);
//    }
//
//    @Override
//    protected void tick(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
//        if (tick++ % 5 != 0) return;
//        MemoryUtil.getWorkPos(maid).ifPresent(posWrapper -> {
//            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
//            if (blockEntity instanceof CuttingBoardBlockEntity cuttingBoardBlockEntity) {
//                if (maidHand) {
//                    ItemStack tool = maid.getMainHandItem();
//                    cuttingBoardBlockEntity.processStoredItemUsingTool(tool, null);
//                    maid.swing(InteractionHand.MAIN_HAND);
//                } else {
//                    ItemStack offhandItem = maid.getOffhandItem();
//                    if (offhandItem.is(this.processItem)) {
//                        ItemStack split = offhandItem.split(1);
//                        cuttingBoardBlockEntity.getInventory().insertItem(0, split, false);
//                        maid.swing(InteractionHand.OFF_HAND);
//                    }
//                }
//
//                maidHand = !maidHand;
//            }
//        });
//    }
//
//    @Override
//    protected void stop(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
//        super.stop(worldIn, maid, pGameTime);
//        this.maidRecipesManager.tranOutput2Chest();
//        this.maidRecipesManager.syncInv();
//        MemoryUtil.eraseWorkPos(maid);
//        this.processItem = null;
//        this.maidHand = false;
//        this.tick = 0;
//    }
//}
