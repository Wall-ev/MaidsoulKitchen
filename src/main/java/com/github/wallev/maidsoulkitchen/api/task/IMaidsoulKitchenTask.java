package com.github.wallev.maidsoulkitchen.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.event.MaidMkTaskEnableEvent;
import com.github.wallev.maidsoulkitchen.compat.patchouli.entry.TaskBookEntryType;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IMaidsoulKitchenTask extends IMaidTask {
    public static void putTask(Supplier<Boolean> canAdd, Supplier<IMaidsoulKitchenTask> task) {
        TaskMap.MAP.put(canAdd, task);
    }

    public static TaskMap getTasks() {
        return TaskMap.MAP;
    }

    default TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.OTHER;
    }

    default String getBookEntry() {
        return this.getBookEntryType().name;
    }

    @Override
    default boolean isEnable(EntityMaid maid) {
        return getEventOrElseData(maid, MaidMkTaskEnableEvent::isEnable, IMaidTask.super.isEnable(maid));
    }

    default <T> T getEventOrElseData(EntityMaid maid, Function<MaidMkTaskEnableEvent, T> eventGet, T def) {
        MaidMkTaskEnableEvent event = new MaidMkTaskEnableEvent(maid, this);
        boolean post = MinecraftForge.EVENT_BUS.post(event);
        if (post) {
            return eventGet.apply(event);
        }

        return def;
    }

    @Override
    default List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(EntityMaid maid) {
        return getEventOrElseData(maid, MaidMkTaskEnableEvent::getEnableConditionDesc, IMaidTask.super.getEnableConditionDesc(maid));
    }

    @Override
    default List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        if (maid.level.isClientSide) {
            return Collections.emptyList();
        }

        return (List) this.vCreateBrainTasks(maid);
    }

    List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid);

    @Override
    default List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        List<Pair<Integer, VBehaviorControl>> rideBrainTasks = vCreateRideBrainTasks(maid);
        if (!rideBrainTasks.isEmpty()) {
            return (List) rideBrainTasks;
        }
        return IMaidTask.super.createRideBrainTasks(maid);
    }

    default List<Pair<Integer, VBehaviorControl>> vCreateRideBrainTasks(EntityMaid entityMaid) {
        return Collections.emptyList();
    }


    default IItemHandlerModifiable getInventory(EntityMaid maid) {
        return maid.getAvailableInv(true);
    }

    public class TaskMap extends LinkedHashMap<Supplier<Boolean>, Supplier<IMaidsoulKitchenTask>> {
        private static final TaskMap MAP = new TaskMap();
    }
}
