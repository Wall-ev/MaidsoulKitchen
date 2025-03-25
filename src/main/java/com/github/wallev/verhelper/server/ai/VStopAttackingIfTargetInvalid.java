package com.github.wallev.verhelper.server.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class VStopAttackingIfTargetInvalid {

    public static <E extends Mob> VBehaviorControl create(BiConsumer<E, LivingEntity> pOnStopAttacking) {
        return (VBehaviorControl) new StopAttackingIfTargetInvalid<>(pOnStopAttacking);
    }

    public static <E extends Mob> VBehaviorControl create(Predicate<LivingEntity> pCanStopAttacking) {
        return (VBehaviorControl) new StopAttackingIfTargetInvalid<>(pCanStopAttacking);
    }

    public static <E extends Mob> VBehaviorControl create() {
        return (VBehaviorControl) new StopAttackingIfTargetInvalid<>();
    }

    public static <E extends Mob> VBehaviorControl create(Predicate<LivingEntity> pCanStopAttacking, BiConsumer<E, LivingEntity> pOnStopAttacking, boolean pCanGrowTiredOfTryingToReachTarget) {
        return (VBehaviorControl) new StopAttackingIfTargetInvalid<>(pCanStopAttacking, pOnStopAttacking, pCanGrowTiredOfTryingToReachTarget);
    }

}
