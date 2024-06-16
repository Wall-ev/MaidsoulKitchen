package com.catbert.tlma.task.ai.brain;

import com.catbert.tlma.api.IMaidAddon;
import com.catbert.tlma.api.task.v1.farm.ICompatFarm;
import com.catbert.tlma.api.task.v1.farm.ICompatFarmHandler;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.state.BlockState;

public class MaidCompatFruitMoveTask<T extends ICompatFarmHandler> extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int verticalSearchRange;
    private final ICompatFarm<T> task;
    private final T compatFarmHandler;
    protected int verticalSearchStart;
    private int searchStartY = 3;
    private boolean initSearchStartY = false;
    public MaidCompatFruitMoveTask(EntityMaid maid, ICompatFarm<T> task, float movementSpeed) {
        this(maid, task, movementSpeed, 2);
    }

    public MaidCompatFruitMoveTask(EntityMaid maid, ICompatFarm<T> task, float movementSpeed, int verticalSearchRange) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.task = task;
        this.compatFarmHandler = task.getCompatHandler(maid);
        this.movementSpeed = movementSpeed;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    private static void setWalkAndLookTargetMemories(LivingEntity pLivingEntity, BlockPos walkPos, BlockPos lookPos, float pSpeed, int pDistance) {
        pLivingEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, pSpeed, pDistance));
        pLivingEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
    }

    public T getCompatFarmHandler() {
        return compatFarmHandler;
    }

    private boolean checkOwnerPos(EntityMaid maid, BlockPos mutableBlockPos) {
        if (maid.isHomeModeEnable()) {
            return true;
        }
        return maid.getOwner() != null && mutableBlockPos.closerToCenterThan(maid.getOwner().position(), 8);
    }

    @Override
    protected void start(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        this.searchForDestination(pLevel, pEntity);
    }

    /**
     * 判定条件
     *
     * @param serverLevel  当前实体所处的 world
     * @param entityMaid 当前需要移动的实体
     * @param blockPos      当前检索的 pos
     * @return 是否符合判定条件
     */
    protected boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, BlockPos blockPos) {
        if (!initSearchStartY) {
            initSearchStartY = true;
            searchStartY = ((IMaidAddon)entityMaid).getStartYOffset$tlma();
        }
        ((IMaidAddon)entityMaid).initFakePlayer$tlma();
        BlockState cropState = serverLevel.getBlockState(blockPos);
        return this.task.canHarvest(entityMaid, blockPos, cropState, this.compatFarmHandler);
    }

    protected boolean checkPathReach(EntityMaid maid, BlockPos pos) {
        return maid.canPathReach(pos);
    }

    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid) {
        BlockPos centrePos = maid.getBrainSearchPos();
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y++) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y - 1, z);
                        if (maid.isWithinRestriction(mutableBlockPos) && shouldMoveTo(worldIn, maid, mutableBlockPos.above(this.searchStartY)) && checkPathReach(maid, mutableBlockPos)
                                && checkOwnerPos(maid, mutableBlockPos)) {
                            setWalkAndLookTargetMemories(maid, mutableBlockPos, mutableBlockPos.above(this.searchStartY), this.movementSpeed, 0);
                            maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(mutableBlockPos.above(this.searchStartY)));
                            this.setNextCheckTickCount(5);
                            return;
                        }
                    }
                }
            }
        }
    }
}
