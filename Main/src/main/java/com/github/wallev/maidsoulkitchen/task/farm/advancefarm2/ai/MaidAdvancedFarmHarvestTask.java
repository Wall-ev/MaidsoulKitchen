package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.ai;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager.MaidBehaviorManager;
import com.github.wallev.maidsoulkitchen.init.ModEntities;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.ICropHarvest;
import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;
import java.util.Optional;

public class MaidAdvancedFarmHarvestTask extends Behavior<EntityMaid> implements VBehaviorControl {

    public MaidAdvancedFarmHarvestTask() {
        super(ImmutableMap.of(
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT,
                ModEntities.HARVEST_DATA.get(), MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        Brain<EntityMaid> brain = owner.getBrain();
        return MaidBehaviorManager.get(owner).isIdle() &&  brain.getMemory(ModEntities.HARVEST_DATA.get()).map(cropResult -> {
            Vec3 targetV3d = cropResult.getWalkPos().getCenter();
            if (owner.distanceToSqr(targetV3d) > Math.pow(cropResult.getCloseEnoughDist(), 2)) {
                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
                if (walkTarget.isEmpty() || !walkTarget.get().getTarget().currentPosition().equals(targetV3d)) {
                    brain.eraseMemory(InitEntities.TARGET_POS.get());
                }
                return false;
            }
            return true;
        }).orElse(false);
    }

    protected boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, ICropHarvest.HarvestData harvestData) {
        ICropHarvest rule = ICropHarvest.Handler.getRule(harvestData.getRuleUid());
        return rule.canHarvest(maid, cropPos, cropState) == ICropHarvest.Result.SUCCESS;
    }

    protected ICropHarvest.Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, ICropHarvest.HarvestData harvestData) {
        return ICropHarvest.Handler.getRule(harvestData.getRuleUid()).harvest(maid, harvestData.getLookPos(), maid.level.getBlockState(harvestData.getLookPos()));
    }

    @Override
    protected void start(ServerLevel world, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(ModEntities.HARVEST_DATA.get()).ifPresent(harvestData -> {
            BlockPos cropPos = harvestData.getCropPos();
            BlockState cropState = world.getBlockState(cropPos);

            if (maid.canDestroyBlock(cropPos) && this.canHarvest(maid, cropPos, cropState, harvestData)) {
                ICropHarvest.Result harvest = this.harvest(maid, cropPos, cropState, harvestData);
                if (harvest == ICropHarvest.Result.SUCCESS) {
                    maid.swing(InteractionHand.MAIN_HAND);
                    maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                    maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                }

                if (maid.getOwner() instanceof ServerPlayer serverPlayer) {
                    InitTrigger.MAID_EVENT.trigger(serverPlayer, TriggerType.MAID_FARM);
                }
            }

            CombinedInvWrapper availableInv = maid.getAvailableInv(true);
            List<Integer> slots = ItemsUtil.getFilterStackSlots(availableInv, this::isSeed);
            if (!slots.isEmpty()) {
                for (int slot : slots) {
                    ItemStack seed = availableInv.getStackInSlot(slot);
                    BlockState baseState = world.getBlockState(cropPos);
                    if (this.canPlant(world, maid, seed, cropPos, null, harvestData)) {
                        ItemStack remain = this.plant(world, maid, seed, cropPos, null, harvestData);
                        availableInv.setStackInSlot(slot, remain);
                        maid.swing(InteractionHand.MAIN_HAND);
                        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
                        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                        if (maid.getOwner() instanceof ServerPlayer serverPlayer) {
                            InitTrigger.MAID_EVENT.trigger(serverPlayer, TriggerType.MAID_FARM);
                        }
                        return;
                    }
                }
            }
            maid.getBrain().eraseMemory(ModEntities.HARVEST_DATA.get());
        });
    }

    protected boolean isSeed(ItemStack stack) {
        return !ICropHarvest.Handler.getRules(stack.getItem()).isEmpty();
    }

    public boolean canPlant(ServerLevel worldIn, EntityMaid maid, ItemStack seed, BlockPos cropPos, MaidPathFindingBFS pathFinding, ICropHarvest.HarvestData data) {
        BlockState cropState = worldIn.getBlockState(cropPos);
        for (ICropHarvest rule : ICropHarvest.Handler.getRules(seed.getItem())) {
            if (rule.canPlant(maid, cropPos, cropState, seed)) {
                return true;
            }
//            if (this.ruleCanMoveTo(rule, cropState, worldIn, maid, cropPos, pathFinding, data)) {
//                return true;
//            }
        }
        return false;
    }

    public ItemStack plant(ServerLevel worldIn, EntityMaid maid, ItemStack seed, BlockPos cropPos, MaidPathFindingBFS pathFinding, ICropHarvest.HarvestData data) {
        for (ICropHarvest rule : ICropHarvest.Handler.getRules(seed.getItem())) {
            if (rule.canPlant(maid, cropPos, worldIn.getBlockState(cropPos), seed)) {
                return rule.plant(maid, cropPos, worldIn.getBlockState(cropPos), seed);
            }
//            if (this.ruleCanMoveTo(rule, cropState, worldIn, maid, cropPos, pathFinding, data)) {
//                return true;
//            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        return super.canStillUse(pLevel, pEntity, pGameTime);
    }
}
