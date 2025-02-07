package com.github.wallev.maidsoulkitchen.api;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.event.MaidMkTaskEnableEvent;
import com.mojang.datafixers.util.Pair;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.function.Predicate;

public interface IMaidsoulKitchenTask extends IMaidTask {

    default TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.OTHER;
    }

    default String getBookEntry() {
        return this.getBookEntryType().name;
    }

    @Override
    default boolean isEnable(EntityMaid maid) {
        MaidMkTaskEnableEvent maidMkTaskEnableEvent = new MaidMkTaskEnableEvent(maid, this);
        MinecraftForge.EVENT_BUS.post(maidMkTaskEnableEvent);
        if (!maidMkTaskEnableEvent.isEnable()) {
            return false;
        }

        return IMaidTask.super.isEnable(maid);
    }

    @Override
    default List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(EntityMaid maid) {
        MaidMkTaskEnableEvent maidMkTaskEnableEvent = new MaidMkTaskEnableEvent(maid, this);
        if (MinecraftForge.EVENT_BUS.post(maidMkTaskEnableEvent)) {
            return maidMkTaskEnableEvent.getEnableConditionDesc();
        }

        return IMaidTask.super.getEnableConditionDesc(maid);
    }
}
