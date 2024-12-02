package com.github.wallev.farmsoulkitchen.task.cook.v1.common.action;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;

public interface IMaidAction {
    default void pickupAction(EntityMaid maid) {
        maid.swing(InteractionHand.MAIN_HAND);
        maid.playSound(SoundEvents.ITEM_PICKUP, 1.0F, maid.getRandom().nextFloat() * 0.1F + 1.0F);
    }
}
