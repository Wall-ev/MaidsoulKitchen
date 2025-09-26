package com.github.wallev.maidsoulkitchen.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.compat.patchouli.entry.TaskBookEntryType;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IMaidsoulKitchenTask extends IMaidTask {
    public static void putTask(ResourceLocation uid, Supplier<Boolean> canAdd, Supplier<IMaidsoulKitchenTask> task) {
        TaskInfoMap.TASK.put(uid, new TaskInfoMap(canAdd, task));
    }

    public static Map<ResourceLocation, TaskInfoMap> getTasks() {
        return TaskInfoMap.TASK;
    }

    default TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.OTHER;
    }

    default String getBookEntry() {
        return this.getBookEntryType().name;
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

    public class TaskInfoMap {
        private static final Map<ResourceLocation, TaskInfoMap> TASK = new LinkedHashMap<>();
        private final Supplier<Boolean> contidion;
        private final Supplier<IMaidsoulKitchenTask> task;

        public TaskInfoMap(Supplier<Boolean> contidion, Supplier<IMaidsoulKitchenTask> task) {
            this.contidion = contidion;
            this.task = task;
        }

        public Supplier<Boolean> getContidion() {
            return contidion;
        }

        public Supplier<IMaidsoulKitchenTask> getTask() {
            return task;
        }
    }

    public class TaskMixinMap extends HashMap<ResourceLocation, List<String>> {
        private static final TaskMixinMap MIXIN = new TaskMixinMap();
        public static void putList(ResourceLocation task, String... clz) {
            MIXIN.put(task, Lists.newArrayList(clz));
        }

        public static void putList(ResourceLocation task, List<String> clz) {
            MIXIN.put(task, clz);
        }

        public static boolean isApplyMixin(ResourceLocation task) {
            boolean apply = true;
            for (String targetClass : MIXIN.getOrDefault(task, List.of())) {
                if (!IMskMixinInterface.applyInterfaceMixin(targetClass)) {
                    MaidsoulKitchen.LOGGER.error("MixinError: task: {}, class: {}", task, targetClass);
                    apply = false;
                }
            }
            return apply;
        }
    }
}
