package com.github.wallev.maidsoulkitchen.task.cook.common.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.debug.annotation.SafeRun;
import com.github.wallev.maidsoulkitchen.debug.annotation.TimeRecord;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.checkOwnerPos;
import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.getSearchPos;

public class CookMoveTask<B extends BlockEntity, R extends Recipe<? extends Container>> extends Behavior<EntityMaid> implements VBehaviorControl {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int verticalSearchRange;
    private final ICookTask<B, R> task;
    private final MaidCookManager<R> cm;
    private final AbstractCookRule<B, R> rule;
    private final CookBeBase<B> cookBe;
    protected int verticalSearchStart;

    public CookMoveTask(ICookTask<B, R> task, MaidCookManager<R> cm, AbstractCookRule<B, R> rule, CookBeBase<B> cookBe) {
        this(task, cm, rule, cookBe, ICookTask.MOVE_SPEED, ICookTask.VERTICAL_SEARCH_RANGE);
    }

    public CookMoveTask(ICookTask<B, R> task, MaidCookManager<R> cm, AbstractCookRule<B, R> rule, CookBeBase<B> cookBe, float movementSpeed, int verticalSearchRange) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                MkEntities.WORK_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.task = task;
        this.cm = cm;
        this.rule = rule;
        this.cookBe = cookBe;

        this.movementSpeed = movementSpeed;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);

        this.cm.checkAndInit();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return this.checkExtraStartConditions() && cm.checkAndInit();
    }

    @TimeRecord
    @SafeRun
    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        this.searchForDestination(worldIn, maid);
    }

    @SuppressWarnings("unchecked")
    protected boolean shouldMoveTo(ServerLevel worldIn, EntityMaid maid, BlockPos blockPos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(blockPos);
        if (blockEntity != null && cookBe.isCookBe(blockEntity)) {
            cm.checkAndCreateRecipes();
            cookBe.setBe((B) blockEntity);
            return this.rule.canMoveTo(cookBe, cm);
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
                            MemoryUtil.rememberWorkPos(maid, cookBe.getWalkPos(), mutableBlockPos.immutable(), this.movementSpeed, 0);
                            this.setNextCheckTickCount(5);
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean checkExtraStartConditions() {
        return cm.checkExtraStartConditions();
    }

    private void setMaxCheckRate(int maxCheckRate) {
        cm.setMaxCheckRate(maxCheckRate);
    }

    private void setNextCheckTickCount(int nextCheckTickCount) {
        cm.setNextCheckTickCount(nextCheckTickCount);
    }

}
