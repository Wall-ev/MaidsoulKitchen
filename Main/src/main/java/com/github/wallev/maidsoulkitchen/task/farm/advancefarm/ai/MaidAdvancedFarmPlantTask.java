//package com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai;
//
//import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
//import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
//import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
//import com.github.wallev.maidsoulkitchen.init.ModEntities;
//import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.TaskAdvanceFarm;
//import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.handler.IHarvestCrop;
//import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
//import com.google.common.collect.ImmutableMap;
//import net.minecraft.core.BlockPos;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.ai.Brain;
//import net.minecraft.world.entity.ai.behavior.Behavior;
//import net.minecraft.world.entity.ai.memory.MemoryModuleType;
//import net.minecraft.world.entity.ai.memory.MemoryStatus;
//import net.minecraft.world.entity.ai.memory.WalkTarget;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.items.wrapper.CombinedInvWrapper;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//public class MaidAdvancedFarmPlantTask extends Behavior<EntityMaid> implements VBehaviorControl {
//    private final TaskAdvanceFarm task;
//
//    public MaidAdvancedFarmPlantTask(TaskAdvanceFarm task) {
//        super(ImmutableMap.of(ModEntities.HAR.get(), MemoryStatus.VALUE_PRESENT));
//        this.task = task;
//    }
//
//    @Override
//    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
//        Brain<EntityMaid> brain = owner.getBrain();
//        return brain.getMemory(ModEntities.HAR.get()).map(cropResult -> {
//            Vec3 targetV3d = cropResult.getToMovePos().getCenter();
//            if (owner.distanceToSqr(targetV3d) > Math.pow(cropResult.closeEnoughDist(), 2)) {
//                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
//                if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
//                    brain.eraseMemory(InitEntities.TARGET_POS.get());
//                }
//                return false;
//            }
//            return true;
//        }).orElse(false);
//    }
//
//    private boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult) {
//        IHarvestCrop rule = Objects.requireNonNull(IHarvestCrop.Handler.getRule(cropResult.uid()));
//        return rule.canHarvest(maid, cropPos, cropState, cropResult).isSuccess();
//    }
//
//    @Override
//    protected void start(ServerLevel world, EntityMaid maid, long gameTimeIn) {
//        maid.getBrain().getMemory(ModEntities.HAR.get()).ifPresent(cropResult -> {
//            BlockPos basePos = cropResult.getToMovePos();
//            BlockPos cropPos = basePos;
//            BlockState cropState = world.getBlockState(cropPos);
//            if (maid.canDestroyBlock(cropPos) && this.canHarvest(maid, cropPos, cropState, cropResult)) {
//                Objects.requireNonNull(IHarvestCrop.Handler.getRule(cropResult.uid())).harvest(maid, cropPos, cropState, cropResult);
//                maid.swing(InteractionHand.MAIN_HAND);
//                maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
//                maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
//                if (maid.getOwner() instanceof ServerPlayer serverPlayer) {
//                    InitTrigger.MAID_EVENT.trigger(serverPlayer, TriggerType.MAID_FARM);
//                }
//            }
//
//            CombinedInvWrapper availableInv = maid.getAvailableInv(true);
//            List<Integer> slots = ItemsUtil.getFilterStackSlots(availableInv, task::isSeed);
//            if (!slots.isEmpty()) {
//                for (int slot : slots) {
//                    ItemStack seed = availableInv.getStackInSlot(slot);
//                    BlockState baseState = world.getBlockState(basePos);
//                    if (task.canPlant(maid, basePos, baseState, seed, cropResult)) {
//                        ItemStack remain = task.plant(maid, basePos, baseState, seed, cropResult);
//                        availableInv.setStackInSlot(slot, remain);
//                        maid.swing(InteractionHand.MAIN_HAND);
//                        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
//                        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
//                        if (maid.getOwner() instanceof ServerPlayer serverPlayer) {
//                            InitTrigger.MAID_EVENT.trigger(serverPlayer, TriggerType.MAID_FARM);
//                        }
//                        return;
//                    }
//                }
//            }
//            maid.getBrain().eraseMemory(ModEntities.HAR.get());
//        });
//    }
//}
