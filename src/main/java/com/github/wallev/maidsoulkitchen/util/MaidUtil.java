package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;

public class MaidUtil {

    public static void pickupAction(EntityMaid maid) {
        maid.swing(InteractionHand.MAIN_HAND);
        maid.playSound(SoundEvents.ITEM_PICKUP, 1.0F, maid.getRandom().nextFloat() * 0.1F + 1.0F);
    }

    public static void pickupAction(CookBeBase<?> cookBeBase) {
        pickupAction(cookBeBase.getMaid());
    }

}
