package com.github.wallev.maidsoulkitchen.init.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.CookTaskManager;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class TaskRegister {
    private static final List<LegacyTaskInfo> LEGACY_TASK = new ArrayList<>();

    private TaskRegister() {
    }

    public static void addLegacyTask(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<Boolean> bindConfig, Supplier<IMaidsoulKitchenTask> task, String... mixinClz) {
        addLegacyTask(uid, bindMod, bindConfig, task, Lists.newArrayList(mixinClz));
    }

    public static void addLegacyTask(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<Boolean> bindConfig, Supplier<IMaidsoulKitchenTask> task, List<String> mixinClz) {
        LEGACY_TASK.add(new LegacyTaskInfo(uid, bindMod, bindConfig, task, mixinClz));
    }

    public static void addLegacyCookTask(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<Boolean> bindConfig, Supplier<ICookTask<?, ?>> task, String... mixinClz) {
        CookTaskManager.addLegacyTask(uid, bindMod, bindConfig, task, mixinClz);
    }

    public static void addLegacyCookTask(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<Boolean> bindConfig, Supplier<ICookTask<?, ?>> task, List<String> mixinClz) {
        CookTaskManager.addLegacyTask(uid, bindMod, bindConfig, task, mixinClz);
    }

    private static void registerLegacyCompat() {
//        LegacyCookTaskRegisterHandler.autoRegisterLegacyCompat();
        loadLegacyCompatTask();
    }

    private static void loadLegacyCompatTask() {
        for (LegacyTaskInfo legacy : LEGACY_TASK) {
            if (!legacy.bindModLoad()) {
                continue;
            }

            ResourceLocation taskUid = legacy.getUid();
            IMaidsoulKitchenTask.putTask(taskUid, () -> {
                return legacy.bindConfigLoad() && IMaidsoulKitchenTask.TaskMixinMap.isApplyMixin(taskUid);
            }, legacy.bindTask());
            if (!legacy.mixinClz().isEmpty()) {
                IMaidsoulKitchenTask.TaskMixinMap.putList(taskUid, legacy.mixinClz());
            }
        }
    }

    public static void init(TaskManager taskManager) {
        registerLegacyCompat();
        MaidsoulKitchenTask.register(taskManager);
        CookTaskManager.init();
    }

    public record LegacyTaskInfo(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod,
                                 Supplier<Boolean> bindConfig,
                                 Supplier<IMaidsoulKitchenTask> bindTask, List<String> mixinClz) {
        public LegacyTaskInfo(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<Boolean> bindConfig, Supplier<IMaidsoulKitchenTask> bindTask, String... mixinClz) {
            this(uid, bindMod, bindConfig, bindTask, Lists.newArrayList(mixinClz));
        }

        public ResourceLocation getUid() {
            return uid.get();
        }

        public boolean bindConfigLoad() {
            return bindConfig.get();
        }

        public boolean bindModLoad() {
            return bindMod.get().versionLoad();
        }
    }
}
