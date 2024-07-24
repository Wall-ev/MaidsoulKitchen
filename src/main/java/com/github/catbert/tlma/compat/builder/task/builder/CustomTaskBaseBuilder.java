package com.github.catbert.tlma.compat.builder.task.builder;

import com.github.catbert.tlma.compat.builder.task.ab.CustomTaskABBaseBuilder;
import net.minecraft.resources.ResourceLocation;

public class CustomTaskBaseBuilder extends CustomTaskABBaseBuilder<CustomTaskBase> {
    protected CustomTaskBaseBuilder(ResourceLocation id) {
        super(id);
    }

    public CustomTaskBaseBuilder(ResourceLocation id, boolean debug) {
        super(id, debug);
    }

    public static CustomTaskBaseBuilder create(ResourceLocation id) {
        return new CustomTaskBaseBuilder(id);
    }

    @Override
    protected CustomTaskBase getTask() {
        return new CustomTaskBase(this);
    }
}
