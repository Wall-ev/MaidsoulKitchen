package com.github.catbert.tlma.compat.builder.task.builder;

import com.github.catbert.tlma.compat.builder.task.ab.CustomTaskABRangedAttackBuilder;
import net.minecraft.resources.ResourceLocation;

public class CustomTaskRangedAttackBuilder extends CustomTaskABRangedAttackBuilder<CustomTaskRangedAttack> {
    protected CustomTaskRangedAttackBuilder(ResourceLocation id) {
        super(id);
    }

    public CustomTaskRangedAttackBuilder(ResourceLocation id, boolean debug) {
        super(id, debug);
    }

    public static CustomTaskRangedAttackBuilder create(ResourceLocation id) {
        return new CustomTaskRangedAttackBuilder(id);
    }

    @Override
    protected CustomTaskRangedAttack getTask() {
        return new CustomTaskRangedAttack(this);
    }
}
