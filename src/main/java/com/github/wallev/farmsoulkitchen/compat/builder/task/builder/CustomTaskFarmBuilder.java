package com.github.wallev.farmsoulkitchen.compat.builder.task.builder;

import com.github.wallev.farmsoulkitchen.compat.builder.task.ab.CustomTaskABFarmBuilder;
import net.minecraft.resources.ResourceLocation;

public class CustomTaskFarmBuilder extends CustomTaskABFarmBuilder<CustomTaskFarm> {

    protected CustomTaskFarmBuilder(ResourceLocation id) {
        super(id);
    }

    public CustomTaskFarmBuilder(ResourceLocation id, boolean debug) {
        super(id, debug);
    }

    @Override
    protected CustomTaskFarm getTask() {
        return new CustomTaskFarm(this);
    }

    public static CustomTaskFarmBuilder create(ResourceLocation id) {
        return new CustomTaskFarmBuilder(id);
    }
}
