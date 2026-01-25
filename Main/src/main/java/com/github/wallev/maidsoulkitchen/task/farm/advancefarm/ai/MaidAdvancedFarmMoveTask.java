//package com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai;
//
//import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
//import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
//import com.github.wallev.maidsoulkitchen.init.ModEntities;
//import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.TaskAdvanceFarm;
//import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
//import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
//import com.google.common.collect.ImmutableMap;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.NonNullList;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
//import net.minecraft.world.entity.ai.memory.MemoryModuleType;
//import net.minecraft.world.entity.ai.memory.MemoryStatus;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraftforge.items.IItemHandler;
//
//import java.util.Map;
//
//public class MaidAdvancedFarmMoveTask extends MaidCheckRateTask implements VBehaviorControl {
//    private static final int MAX_DELAY_TIME = 120;
//    private final float movementSpeed;
//    private final int verticalSearchRange;
//    protected int verticalSearchStart;
//    /**
//     * 最近工作点标志位（用于记录当前工作的方块位置，缓存下来便于下次在该点附近工作）
//     */
//    private BlockPos currentWorkPos;
//
//    private final NonNullList<ItemStack> seeds = NonNullList.create();
//    private final TaskAdvanceFarm task;
//
//    public MaidAdvancedFarmMoveTask(TaskAdvanceFarm task, float movementSpeed) {
//        this(task, movementSpeed, 1);
//    }
//
//    public MaidAdvancedFarmMoveTask(TaskAdvanceFarm task, float movementSpeed, int verticalSearchRange) {
//        this(
//                ImmutableMap.of(
//                        MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
//                        InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT
//                ),
//                movementSpeed, verticalSearchRange, task
//        );
//    }
//
//
//    public MaidAdvancedFarmMoveTask(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryStateIn, float movementSpeed, int verticalSearchRange, TaskAdvanceFarm task) {
//        super(requiredMemoryStateIn);
//        this.movementSpeed = movementSpeed;
//        this.verticalSearchRange = verticalSearchRange;
//        this.setMaxCheckRate(MAX_DELAY_TIME);
//        this.task = task;
//    }
//
//
//    @Override
//    protected void start(ServerLevel worldIn, EntityMaid entityIn, long gameTimeIn) {
//        seeds.clear();
//        IItemHandler inv = entityIn.getAvailableInv(true);
//        for (int i = 0; i < inv.getSlots(); i++) {
//            ItemStack stack = inv.getStackInSlot(i);
//            if (task.isSeed(stack)) {
//                seeds.add(stack);
//            }
//        }
//        this.searchForDestination(worldIn, entityIn);
//    }
//
//    protected boolean shouldMoveTo(ServerLevel worldIn, EntityMaid maid, BlockPos basePos, CropResult cropResult, MaidPathFindingBFS pathFinding) {
////        if (task.checkCropPosAbove()) {
////            BlockPos above2Pos = basePos.above(2);
////            BlockState stateUp2 = worldIn.getBlockState(above2Pos);
////            if (!stateUp2.getCollisionShape(worldIn, above2Pos).isEmpty()) {
////                return false;
////            }
////        }
//
//        BlockPos cropPos = basePos.above();
//        BlockState cropState = worldIn.getBlockState(cropPos);
//        if (task.canHarvest(maid, cropPos, cropState, cropResult, pathFinding)) {
//            return true;
//        }
//
//        BlockState baseState = worldIn.getBlockState(basePos);
//        return seeds.stream().anyMatch(seed -> task.canPlant(maid, basePos, baseState, seed, cropResult));
//    }
//
//
//    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid) {
//        MaidPathFindingBFS pathFinding = getOrCreateArrivalMap(worldIn, maid);
//        BlockPos centrePos = this.getWorkSearchPos(maid);
//        int searchRange = (int) maid.getRestrictRadius();
//        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
//        CropResult cropResult = new CropResult();
//        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
//            for (int i = 0; i < searchRange; ++i) {
//                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
//                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
//                        mutableBlockPos.setWithOffset(centrePos, x, y - 1, z);
//                        cropResult.setCropPos(mutableBlockPos);
//
//                        if (maid.isWithinRestriction(mutableBlockPos) && checkOwnerPos(maid, mutableBlockPos) &&
//                                shouldMoveTo(worldIn, maid, mutableBlockPos, cropResult, pathFinding)) {
//                            MemoryUtil.setWalkAndLookTargetMemories(maid, cropResult.getToMovePos(), cropResult.getToLookPos(), this.movementSpeed, 0);
//                            maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(mutableBlockPos));
//                            maid.getBrain().setMemory(ModEntities.HAR.get(), cropResult);
//                            this.currentWorkPos = mutableBlockPos;
//                            this.setNextCheckTickCount(5);
//                            this.clearCurrentArrivalMap(pathFinding);
//                            return;
//                        }
//                    }
//                }
//            }
//        }
//        this.currentWorkPos = null;
//        this.clearCurrentArrivalMap(pathFinding);
//    }
//
//    protected void clearCurrentArrivalMap(MaidPathFindingBFS pathFinding) {
//        pathFinding.finish();
//    }
//
//    /**
//     * 获取可达性地图的寻路对象
//     */
//    protected MaidPathFindingBFS getOrCreateArrivalMap(ServerLevel worldIn, EntityMaid maid) {
//        return new MaidPathFindingBFS(maid.getNavigation().getNodeEvaluator(), worldIn, maid);
//    }
//
//    // 获取工作的搜寻中心点
//    protected BlockPos getWorkSearchPos(EntityMaid maid) {
//        if (maid.hasRestriction()) {
//            // 当且仅当开启home模式，并且工作点在工作范围内才返回最近工作点
//            if (this.currentWorkPos != null && maid.isWithinRestriction(currentWorkPos)) {
//                return this.currentWorkPos;
//            } else {
//                return maid.getRestrictCenter();
//            }
//        } else {
//            return maid.blockPosition();
//        }
//    }
//
//    protected boolean checkOwnerPos(EntityMaid maid, BlockPos mutableBlockPos) {
//        if (maid.isHomeModeEnable()) {
//            return true;
//        }
//        return maid.getOwner() != null && mutableBlockPos.closerToCenterThan(maid.getOwner().position(), 8);
//    }
//
//}
