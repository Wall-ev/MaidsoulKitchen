package com.github.wallev.maidsoulkitchen.task.cook.common.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.util.ErrorUtil;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.checkOwnerPos;
import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.getSearchPos;

public class MaidCookMoveTask<B extends BlockEntity, R extends Recipe<? extends Container>> extends MaidCheckRateTask implements VBehaviorControl {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int verticalSearchRange;
    private final ICookTask<B, R> task;
    private final MaidRecipesManager2<R> rm;
    private final AbstractCookRule<B, R> rule;
    private final CookBeBase<B> cookBe;
    protected int verticalSearchStart;


    public MaidCookMoveTask(ICookTask<B, R> task, MaidRecipesManager2<R> rm, AbstractCookRule<B, R> rule, CookBeBase<B> cookBe) {
        this(task, rm, rule, cookBe, ICookTask.MOVE_SPEED, ICookTask.VERTICAL_SEARCH_RANGE);
    }

    public MaidCookMoveTask(ICookTask<B, R> task, MaidRecipesManager2<R> rm, AbstractCookRule<B, R> rule, CookBeBase<B> cookBe, float movementSpeed, int verticalSearchRange) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT,
                MkEntities.WORK_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.task = task;
        this.rm = rm;
        this.rule = rule;
        this.cookBe = cookBe;

        this.movementSpeed = movementSpeed;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        ErrorUtil.safeRun(maid, () -> {
            this.searchForDestination(worldIn, maid);
        });
    }

    private boolean processRecipeManager() {
        return this.rm.checkAndCreateRecipesIngredients();
    }

    @SuppressWarnings("unchecked")
    protected boolean shouldMoveTo(ServerLevel worldIn, EntityMaid maid, BlockPos blockPos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(blockPos);
        if (blockEntity == null) {
            return false;
        }
        if (cookBe.isCookBe(blockEntity)) {
            boolean processed = this.processRecipeManager();
            if (!processed) return false;
            cookBe.setBe((B) blockEntity);
            return this.rule.canMoveTo(cookBe, rm);
        }
        return false;
    }

    protected boolean checkPathReach(EntityMaid maid, BlockPos pos) {
        return maid.canPathReach(pos);
    }

    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid) {
        BlockPos centrePos = getSearchPos(maid);
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y + 1, z);

                        if (maid.isWithinRestriction(mutableBlockPos) && shouldMoveTo(worldIn, maid, mutableBlockPos)
//                                && checkPathReach(maid, mutableBlockPos)
                                && checkOwnerPos(maid, mutableBlockPos)) {
                            MemoryUtil.rememberWorkPos(maid, mutableBlockPos.immutable(), this.movementSpeed, 0);
//                            debugInfo(maid, mutableBlockPos);
                            this.setNextCheckTickCount(5);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void debugInfo(EntityMaid maid, BlockPos pos) {
        BlockState blockState = maid.level.getBlockState(pos);
        MaidsoulKitchen.LOGGER.debug("{} MoveTo {} {}", maid, pos, blockState);
    }
}
