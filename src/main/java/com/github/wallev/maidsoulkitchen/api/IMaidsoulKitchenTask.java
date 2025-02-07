package com.github.wallev.maidsoulkitchen.api;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.event.MaidMkTaskEnableEvent;
import net.minecraftforge.common.MinecraftForge;

public interface IMaidsoulKitchenTask extends IMaidTask {

    default TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.OTHER;
    }

    default String getBookEntry() {
        return this.getBookEntryType().name;
    }

    @Override
    default boolean isEnable(EntityMaid maid) {
        return IMaidTask.super.isEnable(maid) && !MinecraftForge.EVENT_BUS.post(new MaidMkTaskEnableEvent(maid, this));
    }
}
