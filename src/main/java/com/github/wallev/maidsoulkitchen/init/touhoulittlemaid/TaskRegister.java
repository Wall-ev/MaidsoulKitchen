package com.github.wallev.maidsoulkitchen.init.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.TaskMixinManager;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class TaskRegister {
    private static final List<LegacyTaskInfo> LEGACY_TASK = new ArrayList<>();

    private TaskRegister() {
    }

    public static boolean clazzLoad(TaskInfo taskInfo) {
        return TaskMixinManager.clazzLoad(taskInfo.uid);
    }

    public static boolean clazzLoad(ResourceLocation taskUid) {
        return TaskMixinManager.clazzLoad(taskUid);
    }

    public static void addLegacyTask(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, Supplier<IMaidsoulKitchenTask> task, String... mixinClz) {
        addLegacyTask(uid, bindMod, bindConfig, task, Lists.newArrayList(mixinClz));
    }

    public static void addLegacyTask(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, Supplier<IMaidsoulKitchenTask> task, List<String> mixinClz) {
        LEGACY_TASK.add(new LegacyTaskInfo(uid, bindMod, bindConfig, task, mixinClz));
    }

    private static void registerLegacyCompat() {
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

    public static void init(TaskManager manager) throws IOException {
        registerLegacyCompat();
        TaskMixinManager.startReadTask();

        IMaidsoulKitchenTask.getTasks().forEach((key, value) -> {
            if (value.getContidion().get()) {
                IMaidsoulKitchenTask maidsoulKitchenTask = value.getTask().get();
                manager.add(maidsoulKitchenTask);
                if (maidsoulKitchenTask instanceof ICookTask<?, ?> cookTask) {
                    ICookTask.putTask(cookTask.getUid(), cookTask);
                }
            }
        });

        TaskMixinManager.clear();
    }

    public record LegacyTaskInfo(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod,
                                 Supplier<ForgeConfigSpec.BooleanValue> bindConfig,
                                 Supplier<IMaidsoulKitchenTask> bindTask, List<String> mixinClz) {
        public LegacyTaskInfo(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, Supplier<IMaidsoulKitchenTask> bindTask, String... mixinClz) {
            this(uid, bindMod, bindConfig, bindTask, Lists.newArrayList(mixinClz));
        }

        public ResourceLocation getUid() {
            return uid.get();
        }

        public boolean bindConfigLoad() {
            return bindConfig.get().get();
        }

        public boolean bindModLoad() {
            return bindMod.get().versionLoaded;
        }
    }
}
