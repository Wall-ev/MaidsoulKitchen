package com.github.wallev.maidsoulkitchen.task.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.init.MkMemories;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public abstract class MaidMoveToTreeTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int verticalSearchRange;
    protected int verticalSearchStart;

    public MaidMoveToTreeTask(float movementSpeed) {
        this(movementSpeed, 1);
    }

    public MaidMoveToTreeTask(float movementSpeed, int verticalSearchRange) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.movementSpeed = movementSpeed;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid) {
        BlockPos centrePos = maid.getBrainSearchPos();
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y - 1, z);
                        if (maid.isWithinRestriction(mutableBlockPos) && shouldMoveTo(worldIn, maid, mutableBlockPos) && checkPathReach(maid, mutableBlockPos)
                                && checkOwnerPos(maid, mutableBlockPos)) {
                            BehaviorUtils.setWalkAndLookTargetMemories(maid, mutableBlockPos, this.movementSpeed, 0);
                            maid.getBrain().setMemory(MkMemories.DESTROY_POS.get(), new BlockPosTracker(mutableBlockPos));
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void start(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        this.searchForDestination(pLevel, pEntity);
    }

    private boolean checkOwnerPos(EntityMaid maid, BlockPos mutableBlockPos) {
        if (maid.isHomeModeEnable()) {
            return true;
        }
        return maid.getOwner() != null && mutableBlockPos.closerToCenterThan(maid.getOwner().position(), 8);
    }

    /**
     * 判定条件
     *
     * @param worldIn  当前实体所处的 world
     * @param entityIn 当前需要移动的实体
     * @param pos      当前检索的 pos
     * @return 是否符合判定条件
     */
    protected abstract boolean shouldMoveTo(ServerLevel worldIn, EntityMaid entityIn, BlockPos pos);

    protected boolean checkPathReach(EntityMaid maid, BlockPos pos) {
        return true;
    }
}
