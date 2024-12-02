package com.github.wallev.farmsoulkitchen.compat.builder.ai.ab;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;
import java.util.function.BiPredicate;

public abstract class CustomABCheckTask extends MaidCheckRateTask implements IAcceptThis{
    protected BiPredicate<ServerLevel, EntityMaid> checkExtraStartConditions;
    int maxCheckRate;
    int nextCheckTickCount;

    public CustomABCheckTask(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryStateIn) {
        super(requiredMemoryStateIn);
    }

    public CustomABCheckTask(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryStateIn, int duration) {
        super(requiredMemoryStateIn, duration);
    }

    public CustomABCheckTask(Map<MemoryModuleType<?>, MemoryStatus> requiredMemoryStateIn, int durationMinIn, int durationMaxIn) {
        super(requiredMemoryStateIn, durationMinIn, durationMaxIn);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return checkExtraStartConditions != null ? checkExtraStartConditions.test(worldIn, owner) : superCheckExtraStartConditions(worldIn, owner);
    }

    public boolean superCheckExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return super.checkExtraStartConditions(worldIn, owner);
    }

//    public interface
}
