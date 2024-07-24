package com.github.catbert.tlma.compat.builder.ai.builder;

import com.github.catbert.tlma.compat.builder.ai.ab.CustomABBehaviorBuilder;
import net.minecraft.resources.ResourceLocation;

public class CustomBehaviorBuilder extends CustomABBehaviorBuilder<CustomBehavior> {
    public CustomBehaviorBuilder(ResourceLocation id) {
        super(id);
    }

    public CustomBehaviorBuilder(ResourceLocation id, boolean debug) {
        super(id, debug);
    }

    @Override
    protected CustomBehavior getT() {
        return new CustomBehavior(entryCondition);
    }
}
