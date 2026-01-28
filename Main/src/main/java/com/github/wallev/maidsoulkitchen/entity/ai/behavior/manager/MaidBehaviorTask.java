package com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.work.IMaidBehavior;
import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Map;

public class MaidBehaviorTask extends Behavior<EntityMaid> implements VBehaviorControl {
    public MaidBehaviorTask() {
        super(Map.of());
    }

    @Override
    protected void start(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        getBehavior(maid).start(serverLevel, maid, gameTime);
    }

    @Override
    protected void tick(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        getBehavior(maid).tick(serverLevel, maid, gameTime);
    }

    @Override
    protected void stop(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        getBehavior(maid).stop(serverLevel, maid, gameTime);
        MaidBehaviorManager.set(maid, BehaviorType.IDLE);
        eraseMemory(maid);
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        if (!MaidBehaviorManager.get(maid).shouldWork()) {
            return false;
        }
        return getBehavior(maid).canStillUse(serverLevel, maid, gameTime);
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return false;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid) {
        if (!MaidBehaviorManager.get(maid).shouldWork()) {
            return false;
        }
        return getBehavior(maid).checkExtraStartConditions(serverLevel, maid);
    }

    private IMaidBehavior getBehavior(EntityMaid maid) {
        BehaviorType behaviorType = MaidBehaviorManager.get(maid);
        ResourceLocation behaviorTypeUid = behaviorType.getUid();
        return MaidBehaviorManager.get(behaviorTypeUid);
    }

    private void eraseMemory(EntityMaid maid) {
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}
