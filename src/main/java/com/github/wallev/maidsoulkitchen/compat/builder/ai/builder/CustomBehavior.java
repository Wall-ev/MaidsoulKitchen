package com.github.wallev.maidsoulkitchen.compat.builder.ai.builder;

import com.github.wallev.maidsoulkitchen.compat.builder.ai.ab.CustomABBehavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class CustomBehavior extends CustomABBehavior<CustomBehaviorBuilder> {
    public CustomBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition) {
        super(pEntryCondition);
    }

    public CustomBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int pDuration) {
        super(pEntryCondition, pDuration);
    }

    public CustomBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int pMinDuration, int pMaxDuration) {
        super(pEntryCondition, pMinDuration, pMaxDuration);
    }

}
