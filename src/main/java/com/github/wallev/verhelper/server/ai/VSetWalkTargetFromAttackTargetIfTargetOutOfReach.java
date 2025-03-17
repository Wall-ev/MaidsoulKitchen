package com.github.wallev.verhelper.server.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;

import java.util.function.Function;

public class VSetWalkTargetFromAttackTargetIfTargetOutOfReach {

    private VSetWalkTargetFromAttackTargetIfTargetOutOfReach() {
    }

    public static VBehaviorControl create(float speedModifier) {
        return (VBehaviorControl) SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(speedModifier);
    }

    public static VBehaviorControl create(Function<LivingEntity, Float> speedModifier) {
        return (VBehaviorControl) SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(speedModifier);
    }
}
