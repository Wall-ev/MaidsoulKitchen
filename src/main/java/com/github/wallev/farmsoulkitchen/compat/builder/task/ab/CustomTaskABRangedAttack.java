package com.github.wallev.farmsoulkitchen.compat.builder.task.ab;

import com.github.wallev.farmsoulkitchen.util.function.Consumer4;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;

public abstract class CustomTaskABRangedAttack<T extends CustomTaskABRangedAttackBuilder> extends CustomTaskABAttack<T> implements IRangedAttackTask {

    protected Consumer4<IRangedAttackTask, EntityMaid, LivingEntity, Float> performRangedAttack;

    public CustomTaskABRangedAttack(T t) {
        super(t);
    }

    @Override
    public void performRangedAttack(EntityMaid shooter, LivingEntity target, float distanceFactor) {
        this.performRangedAttack.accept(this, shooter, target, distanceFactor);
    }

    @Override
    public void initBuilderData(T t) {
        super.initBuilderData(t);
        this.performRangedAttack = t.performRangedAttack;
    }
}
