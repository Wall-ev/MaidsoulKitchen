package com.github.wallev.maidsoulkitchen.handler;

import net.minecraft.world.entity.ai.behavior.MeleeAttack;

@SuppressWarnings("all")
public class VMeleeAttack {

    public static VBehaviorControl create(int pCooldownBetweenAttacks) {
        return (VBehaviorControl) new MeleeAttack(pCooldownBetweenAttacks);
    }

}
