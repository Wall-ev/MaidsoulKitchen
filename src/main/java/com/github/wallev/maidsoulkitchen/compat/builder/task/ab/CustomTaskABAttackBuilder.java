package com.github.wallev.maidsoulkitchen.compat.builder.task.ab;

import com.github.wallev.maidsoulkitchen.util.function.Predicate3;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public abstract class CustomTaskABAttackBuilder<T extends CustomTaskABAttack> extends CustomTaskABBaseBuilder<T>{

    // IAttackTask && default
    protected Predicate3<IAttackTask, EntityMaid, LivingEntity> canAttack;
    protected Predicate3<IAttackTask, EntityMaid, Entity> hasExtraAttack;
    protected Predicate3<IAttackTask, EntityMaid, Entity> doExtraAttack;

    protected CustomTaskABAttackBuilder(ResourceLocation id) {
        super(id);
    }

    public CustomTaskABAttackBuilder(ResourceLocation id, boolean debug) {
        super(id, debug);
    }

    // IAttackTask && default
    public CustomTaskABAttackBuilder<T> canAttack(Predicate3<IAttackTask, EntityMaid, LivingEntity> canAttack) {
        this.canAttack = canAttack;
        return this;
    }

    public CustomTaskABAttackBuilder<T> hasExtraAttack(Predicate3<IAttackTask, EntityMaid, Entity> hasExtraAttack) {
        this.hasExtraAttack = hasExtraAttack;
        return this;
    }

    public CustomTaskABAttackBuilder<T> doExtraAttack(Predicate3<IAttackTask, EntityMaid, Entity> doExtraAttack) {
        this.doExtraAttack = doExtraAttack;
        return this;
    }

}
