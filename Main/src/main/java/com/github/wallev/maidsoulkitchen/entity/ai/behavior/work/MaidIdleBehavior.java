package com.github.wallev.maidsoulkitchen.entity.ai.behavior.work;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager.BehaviorType;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager.MaidBehaviorManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

@MaidBehaviorManager.BehaviorAutoRegister
public class MaidIdleBehavior implements IMaidBehavior {
    @Override
    public void start(ServerLevel serverLevel, EntityMaid maid, long gameTime) {

    }

    @Override
    public void tick(ServerLevel serverLevel, EntityMaid maid, long gameTime) {

    }

    @Override
    public void stop(ServerLevel serverLevel, EntityMaid maid, long gameTime) {

    }

    @Override
    public boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid) {
        return false;
    }
    @Override
    public ResourceLocation getUid() {
        return BehaviorType.IDLE.getUid();
    }

    @Override
    public boolean canStillUse(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        return false;
    }
}
