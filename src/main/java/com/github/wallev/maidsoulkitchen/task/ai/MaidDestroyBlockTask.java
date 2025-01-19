package com.github.wallev.maidsoulkitchen.task.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.init.MkMemories;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;


public class MaidDestroyBlockTask extends Behavior<EntityMaid> {
    protected float breakTime;
    protected float breakMaxTime;
    protected float lastBreakProgress = -1;
    protected BlockPos breakPos;
    public MaidDestroyBlockTask() {
        super(ImmutableMap.of(MkMemories.DESTROY_POS.get(), MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 1200);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTime) {
        maid.getBrain().getMemory(MkMemories.DESTROY_POS.get())
            .map(PositionTracker::currentBlockPosition).ifPresent(blockPos -> {
            BlockState blockState = worldIn.getBlockState(blockPos);
            this.breakPos = blockPos;
            this.breakMaxTime = blockState.getBlock().defaultDestroyTime() * 20 * 6;
        });
    }

    @Override
    protected void tick(ServerLevel worldIn, EntityMaid maid, long gameTime) {
//        if (maid.getRandom().nextInt(20) == 0) {
//            maid.level().levelEvent(1019, this.breakPos, 0);
            if (!maid.swinging) {
                maid.swing(maid.getUsedItemHand());
            }
//        }

        ++this.breakTime;
        int i = (int)(this.breakTime / this.breakMaxTime * 10.0F);
        if (i != this.lastBreakProgress) {
            maid.level().destroyBlockProgress(maid.getId(), this.breakPos, i);
            this.lastBreakProgress = i;
        }

        if (this.breakTime >= this.breakMaxTime) {
            maid.destroyBlock(this.breakPos);
            maid.level().levelEvent(1021, this.breakPos, 0);
            maid.level().levelEvent(2001, this.breakPos, Block.getId(maid.level().getBlockState(this.breakPos)));
        }
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid maid, long gameTime) {
//        maid.level().destroyBlockProgress(maid.getId(), this.breakPos, -1);
//        maid.level().gameEvent(maid, GameEvent.BLOCK_DESTROY, this.breakPos);
        maid.level.setBlock(this.breakPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        maid.getBrain().eraseMemory(MkMemories.DESTROY_POS.get());
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.breakPos = null;
        this.breakMaxTime = 0;
        this.breakTime = 0;
    }

    @Override
    protected boolean canStillUse(ServerLevel worldIn, EntityMaid maid, long gameTime) {
        return this.canStart(maid) && this.breakTime <= breakMaxTime;
    }

    protected boolean canStart(EntityMaid maid) {
        if (this.breakPos != null &&
                net.minecraftforge.common.ForgeHooks.canEntityDestroy(maid.level(), this.breakPos, maid)) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid pOwner) {
        Brain<EntityMaid> brain = pOwner.getBrain();
        return brain.getMemory(MkMemories.DESTROY_POS.get()).map(targetPos -> {
            Vec3 targetV3d = targetPos.currentPosition();
            return !(pOwner.distanceToSqr(targetV3d) > 2);
        }).orElse(false);
    }

    protected float getBreakTime() {
        return Math.max(this.breakMaxTime, this.breakTime);
    }
}
