package com.github.wallev.maidsoulkitchen.config;

import com.github.wallev.maidsoulkitchen.config.subconfig.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class GeneralConfig {

    private static ForgeConfigSpec initCommonConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        TaskConfig.init(builder);
        return builder.build();
    }

    private static ForgeConfigSpec initTaskRegisterConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        RegisterConfig.init(builder);
        return builder.build();
    }

    private static ForgeConfigSpec initTaskRegisterConfig3() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        RegisterConfig3.init(builder);
        return builder.build();
    }

    private static ForgeConfigSpec initMaidTaskConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        TaskRegisterConfig.MaidTask.init(builder);
        return builder.build();
    }

    private static ForgeConfigSpec initCookTaskConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        TaskRegisterConfig.CookTask.init(builder);
        return builder.build();
    }

    public static void init() {
        registerConfig(ModConfig.Type.COMMON, GeneralConfig.initCommonConfig());
        registerConfig(ModConfig.Type.COMMON, GeneralConfig.initTaskRegisterConfig(), "task_register");
        registerConfig(ModConfig.Type.COMMON, GeneralConfig.initMaidTaskConfig(), "maid_task");
        registerConfig(ModConfig.Type.COMMON, GeneralConfig.initCookTaskConfig(), "cook_task");
    }

    private static void registerConfig(ModConfig.Type type, IConfigSpec<?> spec) {
        ModContainer mod = ModLoadingContext.get().getActiveContainer();
        String modId = mod.getModId();
        String path = modId + "/" + modId + "-" + type.extension() + ".toml";
        ModLoadingContext.get().registerConfig(type, spec, path);
    }

    private static void registerConfig(ModConfig.Type type, IConfigSpec<?> spec, String fileName) {
        ModContainer mod = ModLoadingContext.get().getActiveContainer();
        String modId = mod.getModId();
        String path = modId + "/" + modId + "-" + type.extension() + "-" + fileName + ".toml";
        ModLoadingContext.get().registerConfig(type, spec, path);
    }
}
