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
import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class CookMakeTask<B extends BlockEntity, R extends Recipe<? extends Container>> extends Behavior<EntityMaid> implements VBehaviorControl {
    private final ICookTask<B, R> task;
    private final MaidCookManager<R> cm;
    private final AbstractCookRule<B, R> rule;
    private final CookBeBase<B> cookBe;

    public CookMakeTask(ICookTask<B, R> task, MaidCookManager<R> cm, AbstractCookRule<B, R> rule, CookBeBase<B> cookBe) {
        super(ImmutableMap.of(MkEntities.WORK_POS.get(), MemoryStatus.VALUE_PRESENT));
        this.task = task;
        this.cm = cm;
        this.rule = rule;
        this.cookBe = cookBe;
    }

    @SafeRun
    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        // fixme: 防止意外的未中断 WorkPosMemory 导致直接绕过数据初始化而导致的游戏崩溃
        if (!cm.inited()) {
            MemoryUtil.resetCookWorkState(maid);
            cm.resetState();
            return false;
        }

        Brain<EntityMaid> brain = maid.getBrain();
        return brain.getMemory(MkEntities.WORK_POS.get()).map(targetPos -> {
            Vec3 targetV3d = targetPos.currentPosition();
            if (maid.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2)) {
                Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
                if (walkTarget.isEmpty()) {
                    MemoryUtil.eraseWorkPos(maid);
                    cm.resetState();
                }
                return false;
            }
            return true;
        }).orElse(false);
    }

    @TimeRecord
    @SafeRun
    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTime) {
        MemoryUtil.getWorkPos(maid).ifPresent(posWrapper -> {
            BlockPos basePos = posWrapper.currentBlockPosition();
            BlockEntity blockEntity = worldIn.getBlockEntity(basePos);
            if (blockEntity != null && cookBe.isCookBe(blockEntity)) {
                this.rule.cookMake(cookBe, cm);
                this.sync();
            }
        });
    }

    @SafeRun
    @Override
    protected void tick(ServerLevel worldIn, EntityMaid maid, long gameTime) {
        rule.tickCookMake(cookBe, cm);
    }

    protected void sync() {
        cookBe.markChanged();
        cm.itemOutput2Chest();
        cm.syncInv();
        cm.updateInvIngredients();
    }

    @TimeRecord
    @SafeRun
    @Override
    protected void stop(ServerLevel worldIn, EntityMaid maid, long gameTime) {
        rule.tickStop(cookBe, cm);
        this.sync();
        cookBe.clear();
        MemoryUtil.eraseWorkPos(maid);
    }

    @SafeRun
    @Override
    protected boolean canStillUse(ServerLevel worldIn, EntityMaid maid, long gameTime) {
        return rule.tickCan(cookBe, cm);
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        return false;
    }
}
