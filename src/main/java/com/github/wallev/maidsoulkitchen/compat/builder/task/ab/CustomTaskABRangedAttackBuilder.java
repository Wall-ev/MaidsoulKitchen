package com.github.wallev.maidsoulkitchen.compat.builder.task.ab;

import com.github.wallev.maidsoulkitchen.util.function.Consumer4;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public abstract class CustomTaskABRangedAttackBuilder<T extends CustomTaskABRangedAttack> extends CustomTaskABAttackBuilder<T>{
    protected Consumer4<IRangedAttackTask, EntityMaid, LivingEntity, Float> performRangedAttack;

    protected CustomTaskABRangedAttackBuilder(ResourceLocation id) {
        super(id);
    }

    public CustomTaskABRangedAttackBuilder(ResourceLocation id, boolean debug) {
        super(id, debug);
    }

    public CustomTaskABRangedAttackBuilder<T> performRangedAttack(Consumer4<IRangedAttackTask, EntityMaid, LivingEntity, Float> performRangedAttack) {
        this.performRangedAttack = performRangedAttack;
        return this;
    }
}
