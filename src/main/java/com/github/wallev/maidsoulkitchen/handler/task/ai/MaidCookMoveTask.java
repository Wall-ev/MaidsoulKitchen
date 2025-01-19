package com.github.wallev.maidsoulkitchen.handler.task.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.AbstractMaidCookBe;
import com.github.wallev.maidsoulkitchen.handler.task.AbstractTaskCook;
import com.github.wallev.maidsoulkitchen.handler.task.handler.MaidRecipesManager;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.github.wallev.maidsoulkitchen.util.EntityMaidUtil.*;

@SuppressWarnings("unchecked")
public class MaidCookMoveTask<MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>>
        extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int verticalSearchRange;
    private final AbstractTaskCook<MCB, B, R> task;
    private final MaidRecipesManager<MCB, B, R> maidRecipesManager;
    private final MCB maidCookBe;
    protected int verticalSearchStart;
    private BlockPos currentWorkPos;

    public MaidCookMoveTask(AbstractTaskCook<MCB, B, R> task, MaidRecipesManager<MCB, B, R> maidRecipesManager, MCB maidCookBe) {
        this(task, maidRecipesManager, maidCookBe, 0.5f, 1);
    }

    public MaidCookMoveTask(AbstractTaskCook<MCB, B, R> task, MaidRecipesManager<MCB, B, R> maidRecipesManager, MCB maidCookBe, float movementSpeed, int verticalSearchRange) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.movementSpeed = movementSpeed;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);

        this.task = task;
        this.maidRecipesManager = maidRecipesManager;
        this.maidCookBe = maidCookBe;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTime) {
        this.searchForDestination(worldIn, maid);
    }

    protected boolean shouldMoveTo(ServerLevel worldIn, EntityMaid maid, BlockPos blockPos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(blockPos);
        if (blockEntity != null && this.task.isCookBE(blockEntity) && this.processRecipeManager()) {
            this.maidCookBe.setCookBe((B) blockEntity);
            return this.task.shouldMoveTo(worldIn, this.maidCookBe);
        }
        return false;
    }

    protected final boolean processRecipeManager() {
        return this.maidRecipesManager.checkAndCreateRecipesIngredients();
    }

    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid) {
        BlockPos centrePos = getWorkSearchCenterPos(maid, this.currentWorkPos);
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y + 1, z);
                        if (maid.isWithinRestriction(mutableBlockPos) && checkOwnerPos(maid, mutableBlockPos) && shouldMoveTo(worldIn, maid, mutableBlockPos)) {
                            setWalkAndLookTargetMemories(maid, mutableBlockPos, mutableBlockPos, this.movementSpeed, 0);
                            maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(mutableBlockPos));
                            this.currentWorkPos = mutableBlockPos;
                            this.setNextCheckTickCount(5);
                            return;
                        }
                    }
                }
            }
        }
        this.currentWorkPos = null;
    }
}
