package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.ai;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.init.ModEntities;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.ICropHarvest;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;

import java.util.Map;

public class MaidAdvancedFarmMoveTask extends MaidCheckRateTask implements VBehaviorControl {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int verticalSearchRange;
    protected int verticalSearchStart;
    /**
     * 最近工作点标志位（用于记录当前工作的方块位置，缓存下来便于下次在该点附近工作）
     */
    private BlockPos currentWorkPos;

    private final NonNullList<ItemStack> seeds = NonNullList.create();

    public MaidAdvancedFarmMoveTask() {
        this(0.5F);
    }

    public MaidAdvancedFarmMoveTask(float movementSpeed) {
        this(movementSpeed, MaidConfig.MAID_WORK_RANGE.get());
    }

    public MaidAdvancedFarmMoveTask(float movementSpeed, int verticalSearchRange) {

        this(
                ImmutableMap.of(
                        MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                        InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT
                ),
                movementSpeed, verticalSearchRange
        );
    }


    public MaidAdvancedFarmMoveTask(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryStateIn, float movementSpeed, int verticalSearchRange) {
        super(requiredMemoryStateIn);
        this.movementSpeed = movementSpeed;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }


    @Override
    protected void start(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
        seeds.clear();
        IItemHandler inv = entityIn.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (this.isSeed(stack)) {
                seeds.add(stack);
            }
        }

        this.searchForDestination(worldIn, entityIn);
    }

    protected boolean isSeed(ItemStack stack) {
        return !ICropHarvest.Handler.getRules(stack.getItem()).isEmpty();
    }

    public boolean canPlant(ServerLevel worldIn, EntityMaid maid, ItemStack seed, BlockPos cropPos, MaidPathFindingBFS pathFinding, ICropHarvest.HarvestData data) {
        BlockState cropState = worldIn.getBlockState(cropPos);
        for (ICropHarvest rule : ICropHarvest.Handler.getRules(seed.getItem())) {
            if (rule.canPlantTo(worldIn, maid, cropPos, cropState, seed, data)) {
                return true;
            }
//            if (this.ruleCanMoveTo(rule, cropState, worldIn, maid, cropPos, pathFinding, data)) {
//                return true;
//            }
        }
        return false;
    }

    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid) {

        MaidPathFindingBFS pathFinding = getOrCreateArrivalMap(worldIn, maid);
        BlockPos centrePos = this.getWorkSearchPos(maid);
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        ICropHarvest.HarvestData harvestData = new ICropHarvest.HarvestData();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y - 1, z);
//                        mutableBlockPos.setWithOffset(centrePos, x, y + 1, z);
                        harvestData.setCropPos(mutableBlockPos);

                        if (maid.isWithinRestriction(mutableBlockPos) && checkOwnerPos(maid, mutableBlockPos) &&
                                this.canMoveTo(worldIn, maid, mutableBlockPos, pathFinding, harvestData)) {
                            ICropHarvest.HarvestData copyData = harvestData.copy();
                            MemoryUtil.setWalkAndLookTargetMemories(maid, copyData.getWalkPos(), copyData.getLookPos(), this.movementSpeed, 0);
                            maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(copyData.getLookPos()));
                            maid.getBrain().setMemory(ModEntities.HARVEST_DATA.get(), copyData);
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
    protected BlockPos getWorkSearchPos(EntityMaid maid) {
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

    protected boolean checkOwnerPos(EntityMaid maid, BlockPos mutableBlockPos) {
        if (maid.isHomeModeEnable()) {
            return true;
        }
        return maid.getOwner() != null && mutableBlockPos.closerToCenterThan(maid.getOwner().position(), 8);
    }

    /**
     * 检查是否可以移动到指定位置
     *
     * @param worldIn   服务器等级对象
     * @param maid      女仆对象
     * @param cropPos   作物所处的位置
     * @param pathFinding 路径查找对象
     * @param data      收获数据对象
     * @return 是否可以移动到指定位置
     */
    public boolean canMoveTo(ServerLevel worldIn, EntityMaid maid, BlockPos cropPos, MaidPathFindingBFS pathFinding, ICropHarvest.HarvestData data) {
        BlockState cropState = worldIn.getBlockState(cropPos);
        for (ICropHarvest rule : ICropHarvest.Handler.getRules(cropState.getBlock())) {
            if (this.ruleCanMoveTo(rule, cropState, worldIn, maid, cropPos, pathFinding, data)) {
                return true;
            }
        }

//        if (!cropState.canBeReplaced())
//            return false;

        if (!cropState.isAir())
            return false;
//        if (!(worldIn.getBlockState(cropPos.below()).getBlock() instanceof FarmBlock))
//            return false;

        return seeds.stream().anyMatch(seed -> this.canPlant(worldIn, maid, seed, cropPos, pathFinding, data));
    }

    protected boolean ruleCanMoveTo(ICropHarvest rule, BlockState cropState, ServerLevel worldIn, EntityMaid maid, BlockPos cropPos, MaidPathFindingBFS pathFinding, ICropHarvest.HarvestData data) {
        return rule.canMoveTo(worldIn, maid, cropPos, pathFinding, data) == ICropHarvest.Result.SUCCESS;
    }

}