package com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager;

import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;

public enum BehaviorType {

    DESTROY_BLOCK(VResourceLocation.ofMod("destroy_block")),
    PLACE_ITEM(VResourceLocation.ofMod("place_block")),
    ATTACK_ENTITY(VResourceLocation.ofMod("attack_entity")),
    THROW_ITEM(VResourceLocation.ofMod("throw_item")),
    USE_ITEM(VResourceLocation.ofMod("use_item")),
    IDLE(VResourceLocation.ofMod("idle")),
    ;

    private final ResourceLocation uid;

    BehaviorType(ResourceLocation uid) {
        this.uid = uid;
    }

    public ResourceLocation getUid() {
        return uid;
    }

    public boolean isIdle() {
        return this == IDLE;
    }

    public boolean shouldWork() {
        return !isIdle();
    }

}
