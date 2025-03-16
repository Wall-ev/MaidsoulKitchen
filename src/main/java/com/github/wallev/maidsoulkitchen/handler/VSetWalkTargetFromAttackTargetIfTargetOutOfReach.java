package com.github.wallev.maidsoulkitchen.handler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;

import java.util.function.Function;

@SuppressWarnings("all")
public class VSetWalkTargetFromAttackTargetIfTargetOutOfReach {
    public static VBehaviorControl create(float speedModifier) {
        return (VBehaviorControl) new SetWalkTargetFromAttackTargetIfTargetOutOfReach(speedModifier);
    }

    public static VBehaviorControl create(Function<LivingEntity, Float> speedModifier) {
        return (VBehaviorControl) new SetWalkTargetFromAttackTargetIfTargetOutOfReach(speedModifier);
    }
}
