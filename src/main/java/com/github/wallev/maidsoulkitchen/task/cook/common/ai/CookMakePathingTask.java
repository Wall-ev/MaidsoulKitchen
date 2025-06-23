package com.github.wallev.maidsoulkitchen.task.cook.common.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.debug.annotation.SafeRun;
import com.github.wallev.maidsoulkitchen.debug.annotation.TimeRecord;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class CookMakePathingTask<B extends BlockEntity> extends Behavior<EntityMaid> implements VBehaviorControl {
    private final float movementSpeed;
    private final CookBeBase<B> cookBe;
    private BlockPos walkPos;
    private Vec3 walkPosVec3;

    public CookMakePathingTask(CookBeBase<B> cookBe) {
        super(ImmutableMap.of(MkEntities.WORK_POS.get(), MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.cookBe = cookBe;
        this.movementSpeed = ICookTask.MOVE_SPEED;
    }

    @TimeRecord
    @SafeRun
    @Override
    protected void start(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        this.walkPos = cookBe.getWalkPos();
        this.walkPosVec3 = walkPos.getCenter();
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        return pEntity.getNavigation().isDone();
    }

    @SafeRun
    @Override
    protected void tick(ServerLevel worldIn, EntityMaid maid, long gameTime) {
        if (!maid.position().closerThan(walkPosVec3, 2)) {
            MemoryUtil.rememberWalkPos(maid, walkPos, movementSpeed, 0);
        }
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        this.walkPos = null;
        this.walkPosVec3 = null;
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }
}
