package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.ai.core;


import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.init.ModEntities;
import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class MaidDestroyTask extends Behavior<EntityMaid> implements VBehaviorControl {
    public MaidDestroyTask() {
        super(ImmutableMap.of(ModEntities.WORLD_BLOCK_MANAGER.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        return this.checkExtraStartConditions(serverLevel, maid);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid) {
       return MaidWorldBlockManager.get(maid).map(maidWorldBlockManager -> {
            BlockPos breakBlockPos = maidWorldBlockManager.getBreakBlockPos();
            if (breakBlockPos != null && this.canReachBlockByJump(maid, breakBlockPos) && !serverLevel.getBlockState(breakBlockPos).isAir()) {
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    protected void start(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        MaidWorldBlockManager.get(maid).ifPresent(MaidWorldBlockManager::initial);
    }

    @SuppressWarnings("all")
    @Override
    protected void tick(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        MaidWorldBlockManager.get(maid).ifPresent(worldBlockManager -> {
            BlockState blockState = worldBlockManager.getBlockState();
            BlockPos breakBlockPos = worldBlockManager.getBreakBlockPos();
            if (worldBlockManager.getCurrentBreakTime() % 5 == 4.0f) {
                SoundType soundType = blockState.getSoundType(maid.level, breakBlockPos, maid);
                Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(soundType.getHitSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 8.0F, soundType.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), breakBlockPos));
            }

            if (worldBlockManager.getCurrentBreakTime() >= worldBlockManager.getMaxBreakTime()) {
                maid.level.destroyBlockProgress(maid.getId(), breakBlockPos, -1);
                maid.destroyBlock(breakBlockPos);
                worldBlockManager.getHeldItem().hurtAndBreak(1, maid, (m) -> m.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                worldBlockManager.reset();

                MaidWorldBlockManager.erase(maid);
                eraseMemory(maid);
            } else {
                maid.level.destroyBlockProgress(maid.getId(), breakBlockPos, worldBlockManager.getLastBreakProgress());
            }

            swim(maid);
            worldBlockManager.incrementBreakTime();
        });
    }

    @Override
    protected void stop(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        MaidWorldBlockManager.get(maid).ifPresent(worldBlockManager -> {
            BlockPos breakBlockPos = worldBlockManager.getBreakBlockPos();
            if (breakBlockPos != null) {
                maid.level.destroyBlockProgress(maid.getId(), breakBlockPos, 0);
            }
        });

        MaidWorldBlockManager.erase(maid);
        eraseMemory(maid);
    }

    private static void swim(EntityMaid maid) {
        if (!maid.isSwingingArms()) {
            maid.swing(InteractionHand.MAIN_HAND);
        }
    }

    private boolean canReachBlockByJump(EntityMaid maid, BlockPos blockPos) {
        return maid.blockPosition().closerThan(blockPos, 2 + 4);
//        return maid.blockPosition().closerThan(blockPos, maid.getMaxBlockReach() + 4);
    }

    private void eraseMemory(EntityMaid maid) {
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}