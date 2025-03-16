package com.github.wallev.maidsoulkitchen.handler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.StartAttacking;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class VStartAttacking {
    public static <E extends Mob> VBehaviorControl create(Predicate<E> pCanAttack, Function<E, Optional<? extends LivingEntity>> pTargetFinder) {
        return (VBehaviorControl) new StartAttacking<>(pCanAttack, pTargetFinder);
    }
    public static <E extends Mob> VBehaviorControl create(Function<E, Optional<? extends LivingEntity>> pTargetFinder) {
        return (VBehaviorControl) new StartAttacking<>(pTargetFinder);
    }
}
