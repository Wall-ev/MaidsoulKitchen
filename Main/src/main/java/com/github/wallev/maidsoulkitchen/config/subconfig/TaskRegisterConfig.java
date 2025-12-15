package com.github.wallev.maidsoulkitchen.config.subconfig;

import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.CookTaskManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class TaskRegisterConfig {

    private static final Map<ResourceLocation, ForgeConfigSpec.BooleanValue> TASK_CONFIG = new HashMap<>();

    public static boolean enabled(ResourceLocation taskUid) {
        return TASK_CONFIG.containsKey(taskUid) ? TASK_CONFIG.get(taskUid).get() : true;
    }

    public static class MaidTask {

        private static final Map<ResourceLocation, ForgeConfigSpec.BooleanValue> MAID_TASK_CONFIG = new HashMap<>();

        public static void init(ForgeConfigSpec.Builder builder) {
            builder.comment("This configure is used to enable or disable the maid task.");
            builder.comment("这是用来开启或关闭女仆任务的配置。");
            Arrays.stream(MaidsoulKitchenTask.values()).forEach(task -> {
                builder.push(task.uid.toString());
                ForgeConfigSpec.BooleanValue define = builder.define("enabled", true);
                MAID_TASK_CONFIG.put(task.uid, define);
                TASK_CONFIG.put(task.uid, define);
                builder.pop();
            });

        }

        public static boolean isEnabled(ResourceLocation taskUid) {
            return MAID_TASK_CONFIG.containsKey(taskUid) ? MAID_TASK_CONFIG.get(taskUid).get() : false;
        }

        public static void setEnabled(ResourceLocation taskUid, boolean enabled) {
            if (MAID_TASK_CONFIG.containsKey(taskUid)) {
                MAID_TASK_CONFIG.get(taskUid).set(enabled);
            }
        }

        public static void acp(BiConsumer<ResourceLocation, ForgeConfigSpec.BooleanValue> config) {
            MAID_TASK_CONFIG.forEach(config);
        }

    }

    public static class CookTask {

        private static final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<?>> COOK_TASK_CONFIG = new HashMap<>();

        public static void init(ForgeConfigSpec.Builder builder) {
            builder.comment("This configure is used to enable or disable the cook task.");
            builder.comment("这是用来开启或关闭烹饪任务的配置。");
            CookTaskManager.getAllTaskUid()
                    .forEach((group, list) -> {
                        builder.push(group);
                        list.forEach(uid -> {
                            builder.push(uid.getPath().replaceAll(group + "_", ""));
                            ForgeConfigSpec.BooleanValue define = builder.define("enabled", true);
                            COOK_TASK_CONFIG.put(uid, define);
                            TASK_CONFIG.put(uid, define);
                            builder.pop();
                        });
                        builder.pop();
                    });
        }

        public static void acp(BiConsumer<ResourceLocation, ForgeConfigSpec.ConfigValue<?>> config) {
            COOK_TASK_CONFIG.forEach(config);
        }
    }
}
