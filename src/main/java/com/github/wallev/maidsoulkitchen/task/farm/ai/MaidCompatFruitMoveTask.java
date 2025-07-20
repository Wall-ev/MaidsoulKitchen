package com.github.wallev.maidsoulkitchen.task.farm.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmTask;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmHandler;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatHandlerInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.state.BlockState;

public class MaidCompatFruitMoveTask<T extends ICompatFarmHandler & ICompatHandlerInfo> extends MaidCheckRateTask implements VBehaviorControl {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int verticalSearchRange;
    private final ICompatFarmTask<T> task;
    private final T compatFarmHandler;
    protected int verticalSearchStart;
    private int searchStartY = 3;
    private boolean initSearchStartY = false;
    /**
     * 最近工作点标志位（用于记录当前工作的方块位置，缓存下来便于下次在该点附近工作）
     */
    private BlockPos currentWorkPos;
    public MaidCompatFruitMoveTask(EntityMaid maid, ICompatFarmTask<T> task, float movementSpeed) {
        this(maid, task, movementSpeed, 2);
    }

    public MaidCompatFruitMoveTask(EntityMaid maid, ICompatFarmTask<T> task, float movementSpeed, int verticalSearchRange) {
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
        initData(pEntity);
        this.searchForDestination(pLevel, pEntity);
    }

    protected boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, BlockPos blockPos) {
        BlockState cropState = serverLevel.getBlockState(blockPos);
        return this.task.canHarvest(entityMaid, blockPos, cropState, this.compatFarmHandler);
    }

    @SuppressWarnings("unchecked")
    private void initData(EntityMaid entityMaid) {
        if (!initSearchStartY) {
            initSearchStartY = true;
            searchStartY = task.getTaskData(entityMaid).searchYOffset();
        }
    }

    // todo
    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid) {
        MaidPathFindingBFS pathFinding = getOrCreateArrivalMap(worldIn, maid);
        BlockPos centrePos = this.getWorkSearchPos(maid);
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange * 2; y++) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y, z);
                        if (maid.isWithinRestriction(mutableBlockPos) && shouldMoveTo(worldIn, maid, mutableBlockPos.above(this.searchStartY)) && checkPathReach(maid, pathFinding, mutableBlockPos)
                                && checkOwnerPos(maid, mutableBlockPos)) {
                            setWalkAndLookTargetMemories(maid, mutableBlockPos, mutableBlockPos.above(this.searchStartY), this.movementSpeed, 0);
                            maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(mutableBlockPos.above(this.searchStartY)));
                            this.currentWorkPos = mutableBlockPos;
                            this.setNextCheckTickCount(5);
                            this.clearCurrentArrivalMap(pathFinding);
                            return;
                        }
                    }
                }
            }
        }
        this.currentWorkPos = null;
        this.clearCurrentArrivalMap(pathFinding);
    }

    protected void clearCurrentArrivalMap(MaidPathFindingBFS pathFinding) {
        pathFinding.finish();
    }

    /**
     * 获取可达性地图的寻路对象
     */
    protected MaidPathFindingBFS getOrCreateArrivalMap(ServerLevel worldIn, EntityMaid maid) {
        return new MaidPathFindingBFS(maid.getNavigation().getNodeEvaluator(), worldIn, maid);
    }

    // 获取工作的搜寻中心点
    private BlockPos getWorkSearchPos(EntityMaid maid) {
        if (maid.hasRestriction()) {
            // 当且仅当开启home模式，并且工作点在工作范围内才返回最近工作点
            if (this.currentWorkPos != null && maid.isWithinRestriction(currentWorkPos)) {
                return this.currentWorkPos;
            } else {
                return maid.getRestrictCenter();
            }
        } else {
            return maid.blockPosition();
        }
    }

    protected boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos) {
        return pathFinding.canPathReach(pos);
    }
}
