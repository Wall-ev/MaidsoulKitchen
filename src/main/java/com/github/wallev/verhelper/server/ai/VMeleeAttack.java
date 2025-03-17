package com.github.wallev.verhelper.server.ai;

import net.minecraft.world.entity.ai.behavior.MeleeAttack;

public class VMeleeAttack {

    private VMeleeAttack() {
    }

    public static VBehaviorControl create(int pCooldownBetweenAttacks) {
        return (VBehaviorControl) MeleeAttack.create(pCooldownBetweenAttacks);
    }

}
