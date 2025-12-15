package com.github.wallev.maidsoulkitchen.config.subconfig;

import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.CookTaskManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterConfig3 {

    private static final Map<ResourceLocation, ForgeConfigSpec.BooleanValue> MAID_TASK_CONFIG = new HashMap<>();
    private static final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<?>> COOK_TASK_CONFIG = new HashMap<>();

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.comment("This configure is used to enable or disable the maid task.");
        builder.comment("这是用来开启或关闭女仆任务的配置。");
        builder.push("maid_task");
        Arrays.stream(MaidsoulKitchenTask.values()).forEach(task -> {
            builder.push(task.uid.toString());
            ForgeConfigSpec.BooleanValue define = builder.define("enabled", true);
            builder.worldRestart();
            MAID_TASK_CONFIG.put(task.uid, define);
            builder.pop();
        });
        builder.pop();

        builder.comment("This configure is used to enable or disable the cook task.");
        builder.comment("这是用来开启或关闭烹饪任务的配置。");
        builder.push("cook_task");
        CookTaskManager.getAllTaskUid()
                .forEach((group, list) -> {
                    builder.push(group);
                    list.forEach(uid -> {
                        builder.push(uid.getPath().replaceAll(group + "_", ""));
                        ForgeConfigSpec.BooleanValue define = builder.define("enabled", true);
                        COOK_TASK_CONFIG.put(uid, define);
                        builder.pop();
                    });
                    builder.pop();
                });
        builder.pop();
    }

}
