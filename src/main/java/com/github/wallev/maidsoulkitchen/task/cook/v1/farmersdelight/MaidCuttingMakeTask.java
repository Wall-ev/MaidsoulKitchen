package com.github.wallev.maidsoulkitchen.task.cook.v1.farmersdelight;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.farmersdelight.TaskFdCuttingBoard;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

import java.util.Optional;

public class MaidCuttingMakeTask extends Behavior<EntityMaid> {
    private final TaskFdCuttingBoard task;
    private final MaidRecipesManager<CuttingBoardRecipe> maidRecipesManager;
    private boolean maidHand = false;
    private int tick = 0;
    private Item processItem = null;

    public MaidCuttingMakeTask(TaskFdCuttingBoard task, MaidRecipesManager<CuttingBoardRecipe> maidRecipesManager) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT), 1200);
        this.task = task;
        this.maidRecipesManager = maidRecipesManager;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        Brain<EntityMaid> brain = maid.getBrain();
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            Vec3 targetV3d = targetPos.currentPosition();
            if (maid.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2)) {
//                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
//                if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
////                    brain.eraseMemory(InitEntities.TARGET_POS.get());
//                }
                return false;
            }
            return true;
        }).orElse(false);
    }

    @Override
    protected boolean canStillUse(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        return maid.getBrain().hasMemoryValue(InitEntities.TARGET_POS.get()) && ((!maid.getOffhandItem().isEmpty() && !maid.getMainHandItem().isEmpty()) || isProcessItem(worldIn, maid));
    }

    private boolean isProcessItem(ServerLevel worldIn, EntityMaid maid) {
        Optional<PositionTracker> tracker = maid.getBrain().getMemory(InitEntities.TARGET_POS.get());

        if (tracker.isPresent()) {
            BlockEntity blockEntity = worldIn.getBlockEntity(tracker.get().currentBlockPosition());
            if (blockEntity instanceof CuttingBoardBlockEntity cuttingBoardBlockEntity) {
                return cuttingBoardBlockEntity.getStoredItem().is(this.processItem);
            }
        }

        return false;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        super.start(worldIn, maid, pGameTime);
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
            if (blockEntity instanceof CuttingBoardBlockEntity cuttingBoardBlockEntity) {
                task.processCookMake(worldIn, maid, cuttingBoardBlockEntity, this.maidRecipesManager, (item) -> {
                    this.processItem = item;
                });
                this.maidRecipesManager.getCookInv().syncInv();
            }
        });
    }

    @Override
    protected void tick(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        super.tick(worldIn, maid, pGameTime);
        if (tick++ % 5 != 0) return;
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
            if (blockEntity instanceof CuttingBoardBlockEntity cuttingBoardBlockEntity) {
                if (maidHand) {
                    ItemStack tool = maid.getMainHandItem();
                    cuttingBoardBlockEntity.processStoredItemUsingTool(tool, null);
                    maid.swing(InteractionHand.MAIN_HAND);
                } else {
                    ItemStack split = maid.getOffhandItem().split(1);
                    cuttingBoardBlockEntity.getInventory().insertItem(0, split, false);
                    maid.swing(InteractionHand.OFF_HAND);
                }

                maidHand = !maidHand;
            }
        });
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        super.stop(worldIn, maid, pGameTime);
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MkEntities.WORK_POS.get());
        this.processItem = null;
        this.maidHand = false;
        this.tick = 0;
    }
}
