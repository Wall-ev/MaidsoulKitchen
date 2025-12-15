package com.github.wallev.maidsoulkitchen.config.subconfig;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlWriter;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.CookTaskManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID)
public class RegisterConfig2 {
    public static final Path CONFIG_BASE_PATH = FMLPaths.CONFIGDIR.get();
    public static final String NAME = "maidsoulkitchen-task_register.toml";

    static CommentedConfig config;
    public static Map<ResourceLocation, Boolean> ENABLED = new HashMap<>();
    public static Map<ResourceLocation, ForgeConfigSpec.BooleanValue> MAID_TASKS_CONFIGS = new HashMap<>();
    public static Map<ResourceLocation, ForgeConfigSpec.BooleanValue> COOK_TASKS_CONFIGS = new HashMap<>();

    protected static <T> T getOrSetAndChange(String path, MutableBoolean change, T defaultValue) {
        return config.getOrElse(path, () -> {
            change.setValue(true);
            config.set(path, defaultValue);
            return defaultValue;
        });
    }

    public static String getByResourceLocation(ResourceLocation resourceLocation) {
        return resourceLocation.getNamespace() + "." + resourceLocation.getPath();
    }

    public static void load() {
//        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
//
//        Path configPath = CONFIG_BASE_PATH.resolve(NAME);
//        TomlParser parser = new TomlParser();
//
//        if (configPath.toFile().exists())
//            try {
//                config = parser.parse(new FileReader(configPath.toFile()));
//            } catch (Exception e) {
//                config = TomlFormat.newConfig();
//            }
//        else
//            config = TomlFormat.newConfig();
//
//        MutableBoolean change = new MutableBoolean(false);
//
//
//
//
//        CraftManager.getInstance().getAutoCraftGuideGenerators()
//                .forEach(generator -> {
//                    ENABLED.put(generator.getType(), getOrSetAndChange(
//                            getByResourceLocation(generator.getType()) + ".enable",
//                            change,
//                            true
//                    ));
//                    List<ConfigTypes.ConfigType<?>> configuration = generator.getConfigurations();
//                    configuration.forEach(configType -> {
//                        configType.read(config, getByResourceLocation(generator.getType()), change);
//                    });
//                    ENABLED.put(generator.getType(), configuration);
//                });
//
//        if (change.getValue()) save();
    }

    public static void save() {
        Path configPath = CONFIG_BASE_PATH.resolve(NAME);
        TomlWriter writer = new TomlWriter();



//        CraftManager.getInstance().getAutoCraftGuideGenerators()
//                .forEach(generator -> {
//                    config.set(getByResourceLocation(generator.getType()) + ".enable", ENABLED.get(generator.getType()));
//                    if (ENABLED.containsKey(generator.getType()))
//                        ENABLED.get(generator.getType()).forEach(configType -> {
//                            configType.save(config, getByResourceLocation(generator.getType()));
//                        });
//                });

        try {
            if (!configPath.toFile().exists())
                configPath.toFile().createNewFile();
            writer.write(config.unmodifiable(), configPath, WritingMode.REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isEnabled(ResourceLocation type) {
        if (!ENABLED.containsKey(type))
            return false;
        return ENABLED.get(type);
    }

    public static void setEnable(ResourceLocation type, Boolean b) {
        ENABLED.put(type, b);
        save();
    }

    public static ForgeConfigSpec initTaskRegisterConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        RegisterConfig2.init0(builder);
        return builder.build();
    }

    public static void init0(ForgeConfigSpec.Builder builder) {
        builder.push("Register0");

        Arrays.stream(MaidsoulKitchenTask.values()).filter(
                task -> !task.uid.equals(MaidsoulKitchenTask.COOK.uid)
        ).forEach(task -> {
            builder.comment("This can make the " + task.uid + " task enabled or not.");
            ForgeConfigSpec.BooleanValue define = builder.define(task.uid + "TaskEnabled", true);
            MAID_TASKS_CONFIGS.put(task.uid, define);
        });

        builder.push("Cook");
        CookTaskManager.getTaskIndex()
                .forEach(cookTask -> {
                    builder.comment("This can make the " + cookTask.getTaskName() + " task enabled or not.");
                    ForgeConfigSpec.BooleanValue define = builder.define(cookTask.getTaskName() + "TaskEnabled", true);
                    MAID_TASKS_CONFIGS.put(cookTask.getUid(), define);
                });
    }
}
