package com.github.wallev.maidsoulkitchen.entity.ai.behavior.work;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public interface IMaidBehavior {

    ResourceLocation getUid();

    boolean canStillUse(ServerLevel serverLevel, EntityMaid maid, long gameTime);

    boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid);

    void start(ServerLevel serverLevel, EntityMaid maid, long gameTime);

    void tick(ServerLevel serverLevel, EntityMaid maid, long gameTime);

    void stop(ServerLevel serverLevel, EntityMaid maid, long gameTime);

}
