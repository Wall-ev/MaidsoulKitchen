package com.github.wallev.maidsoulkitchen.api;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.event.MaidMkTaskEnableEvent;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("all")
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
        MinecraftForge.EVENT_BUS.post(maidMkTaskEnableEvent);
        if (!maidMkTaskEnableEvent.isEnable()) {
            return maidMkTaskEnableEvent.getEnableConditionDesc();
        }

        return IMaidTask.super.getEnableConditionDesc(maid);
    }

    @Override
    default List<Pair<Integer, Behavior<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        return (List) this.vCreateBrainTasks(maid);
    }

    List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid);

    @Override
    default List<Pair<Integer, Behavior<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        List<Pair<Integer, VBehaviorControl>> rideBrainTasks = vCreateRideBrainTasks(maid);
        if (!rideBrainTasks.isEmpty()) {
            return (List) rideBrainTasks;
        }
        return IMaidTask.super.createRideBrainTasks(maid);
    }

    default List<Pair<Integer, VBehaviorControl>> vCreateRideBrainTasks(EntityMaid entityMaid) {
        return Collections.emptyList();
    }
}
