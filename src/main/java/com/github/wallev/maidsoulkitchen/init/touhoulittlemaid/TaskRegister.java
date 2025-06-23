package com.github.wallev.maidsoulkitchen.init.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class TaskRegister {
    private static final List<LegacyTaskInfo> LEGACY_TASK = new ArrayList<>();

    private TaskRegister() {
    }

    public static void addLegacyTask(ResourceLocation uid, ForgeConfigSpec.BooleanValue bindConfig, Supplier<IMaidsoulKitchenTask> task, String... mixinClz) {
        LEGACY_TASK.add(new LegacyTaskInfo(uid, bindConfig, task, mixinClz));
    }

    private static void registerLegacyCompat() {
        for (LegacyTaskInfo legacy : LEGACY_TASK) {
            IMaidsoulKitchenTask.putTask(legacy.uid(), () ->{
                return legacy.bindConfig().get() && IMaidsoulKitchenTask.TaskMixinMap.isApplyMixin(legacy.uid());
            }, legacy.bindTask());
            if (legacy.mixinClz().length > 0) {
                IMaidsoulKitchenTask.TaskMixinMap.put(legacy.uid(), legacy.mixinClz());
            }
        }
    }

    public static void init(TaskManager manager) {
        registerLegacyCompat();

        IMaidsoulKitchenTask.getTasks().forEach((key, value) -> {
            if (value.getContidion().get()) {
                IMaidsoulKitchenTask maidsoulKitchenTask = value.getTask().get();
                manager.add(maidsoulKitchenTask);
                if (maidsoulKitchenTask instanceof ICookTask<?, ?> cookTask) {
                    ICookTask.putTask(cookTask.getUid(), cookTask);
                }
            }
        });
    }

    public record LegacyTaskInfo(ResourceLocation uid, ForgeConfigSpec.BooleanValue bindConfig,
                                 Supplier<IMaidsoulKitchenTask> bindTask, String... mixinClz) {
    }
}
