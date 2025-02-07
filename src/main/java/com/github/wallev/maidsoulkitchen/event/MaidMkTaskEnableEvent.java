package com.github.wallev.maidsoulkitchen.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.IMaidsoulKitchenTask;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class MaidMkTaskEnableEvent extends Event {

    private final EntityMaid maid;
    private final IMaidsoulKitchenTask maidsoulKitchenTask;

    public MaidMkTaskEnableEvent(EntityMaid maid, IMaidsoulKitchenTask maidsoulKitchenTask) {
        this.maid = maid;
        this.maidsoulKitchenTask = maidsoulKitchenTask;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public IMaidsoulKitchenTask getMaidsoulKitchenTask() {
        return maidsoulKitchenTask;
    }
}
