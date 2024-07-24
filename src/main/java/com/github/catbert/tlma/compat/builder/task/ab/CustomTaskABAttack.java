package com.github.catbert.tlma.compat.builder.task.ab;

import com.github.catbert.tlma.util.function.Predicate3;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public abstract class CustomTaskABAttack<T extends CustomTaskABAttackBuilder> extends CustomTaskABBase<T> implements IAttackTask {

    // IAttackTask && default
    protected Predicate3<IAttackTask, EntityMaid, LivingEntity> canAttack;
    protected Predicate3<IAttackTask, EntityMaid, Entity> hasExtraAttack;
    protected Predicate3<IAttackTask, EntityMaid, Entity> doExtraAttack;

    public CustomTaskABAttack(T t) {
        super(t);
    }

    @Override
    public boolean canAttack(EntityMaid maid, LivingEntity target) {
        if (this.canAttack != null) {
            return this.canAttack.test(this, maid, target);
        }
        return IAttackTask.super.canAttack(maid, target);
    }

    @Override
    public boolean hasExtraAttack(EntityMaid maid, Entity target) {
        if (this.hasExtraAttack != null) {
            return this.hasExtraAttack.test(this, maid, target);
        }
        return IAttackTask.super.hasExtraAttack(maid, target);
    }

    @Override
    public boolean doExtraAttack(EntityMaid maid, Entity target) {
        if (this.doExtraAttack != null) {
            return this.doExtraAttack.test(this, maid, target);
        }
        return IAttackTask.super.doExtraAttack(maid, target);
    }

    @Override
    public void initBuilderData(T builder) {
        super.initBuilderData(builder);
        this.canAttack = builder.canAttack;
        this.hasExtraAttack = builder.hasExtraAttack;
        this.doExtraAttack = builder.doExtraAttack;
    }
}
