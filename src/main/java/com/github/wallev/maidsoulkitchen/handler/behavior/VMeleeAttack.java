package com.github.wallev.maidsoulkitchen.handler.behavior;

import net.minecraft.world.entity.ai.behavior.MeleeAttack;

public class VMeleeAttack {

    public static VBehaviorControl create(int pCooldownBetweenAttacks) {
        return (VBehaviorControl) MeleeAttack.create(pCooldownBetweenAttacks);
    }

}
