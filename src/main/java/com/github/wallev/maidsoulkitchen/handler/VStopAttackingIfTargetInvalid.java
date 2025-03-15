package com.github.wallev.maidsoulkitchen.handler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class VStopAttackingIfTargetInvalid {

    public static <E extends Mob> VBehaviorControl create(BiConsumer<E, LivingEntity> pOnStopAttacking) {
        return (VBehaviorControl) StopAttackingIfTargetInvalid.create(pOnStopAttacking);
    }

    public static <E extends Mob> VBehaviorControl create(Predicate<LivingEntity> pCanStopAttacking) {
        return (VBehaviorControl) StopAttackingIfTargetInvalid.create(pCanStopAttacking);
    }

    public static <E extends Mob> VBehaviorControl create() {
        return (VBehaviorControl) StopAttackingIfTargetInvalid.create();
    }

    public static <E extends Mob> VBehaviorControl create(Predicate<LivingEntity> pCanStopAttacking, BiConsumer<E, LivingEntity> pOnStopAttacking, boolean pCanGrowTiredOfTryingToReachTarget) {
        return (VBehaviorControl) StopAttackingIfTargetInvalid.create(pCanStopAttacking, pOnStopAttacking, pCanGrowTiredOfTryingToReachTarget);
    }

}
