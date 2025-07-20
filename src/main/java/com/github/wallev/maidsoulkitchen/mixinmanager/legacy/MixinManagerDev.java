package com.github.wallev.maidsoulkitchen.mixinmanager.legacy;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.mixinmanager.legacy.config.TaskRegisterConfig;
import com.github.wallev.maidsoulkitchen.mixinmanager.legacy.manager.TaskMixinRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mojang.serialization.JsonOps;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class MixinManagerDev {
    private static final String MIXIN_PACKAGE = "com.github.wallev.maidsoulkitchen.mixin";
    private static final String FILE_NAME = "maidsoulkitchen-mixins.json";
    private static final String CONFIG_FILE_PATH = String.format("./%s/%s", "config", FILE_NAME);

    public static final Map<String, JsonConfigBoolean> MIXIN_CONFIG = new HashMap<>();


    static {
        buildConfig();
        Config.init();
    }

    private static void putMixin(String mixinClass, boolean defaultValue, Mods mod) {
        MIXIN_CONFIG.put(mixinClass, new JsonConfigBoolean(mixinClass, defaultValue, mod));
    }

    private static void buildConfig() {
        MIXIN_CONFIG.clear();

        putMixin("farmersdelight.CookingPotBlockEntityMixin", true, Mods.FD);
        putMixin("minersdelight.CopperPotBlockEntityMixin", true, Mods.MD);
        putMixin("youkaishomecoming.BasePotBlockEntityMixin", true, Mods.YHCD);
    }

    protected static boolean isMixinEnabled(String mixinClassName) {
        String mixinClassName1 = mixinClassName.replace(String.format("%s.", MIXIN_PACKAGE), "");
        JsonConfigBoolean mixinConfig = MIXIN_CONFIG.get(mixinClassName1);
        if (mixinConfig == null) {
            return true;
        } else {
            return mixinConfig.canLoaded();
        }
    }

    protected static void loadMixinSettings() {
        File file = new File(CONFIG_FILE_PATH);
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                Type mapType = new TypeToken<Map<String, Boolean>>() {
                }.getType();

                Map<String, Boolean> existMixinList = gson.fromJson(fileReader, mapType);
                existMixinList.forEach((mixinClassName, mixinConfig) -> {
                    MIXIN_CONFIG.get(mixinClassName).set(mixinConfig);
                });

                fileReader.close();
            } catch (Exception e) {
                MaidsoulKitchen.LOGGER.warn("Could not load {} Mixin Configs, creating new config. ERROR: {}", MaidsoulKitchen.MOD_ID, e.getLocalizedMessage());
            }

        } else {
            MaidsoulKitchen.LOGGER.warn("{} Mixin Configs not found, creating new config.", MaidsoulKitchen.MOD_ID);
        }

        saveMixinSettings();
    }

    public static void saveMixinSettings() {
        Gson gson = new Gson();
        File file = new File(CONFIG_FILE_PATH);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            JsonObject mixinFileData = new JsonObject();
            MIXIN_CONFIG.forEach((mixinClassName, mixinConfig) -> {
                mixinFileData.addProperty(mixinClassName, mixinConfig.get());
            });

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(mixinFileData));
            fileWriter.close();
        } catch (IOException e) {
            MaidsoulKitchen.LOGGER.warn("Could not save {} Mixin Configs: {}", MaidsoulKitchen.MOD_ID, e.getLocalizedMessage());
        }
    }

    public static class Config {
        public static TaskRegisterConfig taskRegisterConfig = TaskMixinRegister.create();


        private static final String FILE_NAME = "custom_fruit_handlers.json";
        private static final String CONFIG_FILE_PATH = String.format("./%s/%s/%s", "config", MaidsoulKitchen.MOD_ID, FILE_NAME);

        public static void init() {
        }

        public static void load() {
            File file = new File(CONFIG_FILE_PATH);
            if (!file.exists()) {
                return;
            }

            try {
                String json = Files.readString(file.toPath());
                JsonObject jsonData = JsonParser.parseString(json).getAsJsonObject();
//                JsonArray customsJson = jsonData.getAsJsonArray("customs");
//                for (JsonElement jsonElement : customsJson.asList()) {
//                    JsonObject customJson = jsonElement.getAsJsonObject();
//                    CustomFruitTemplate.read(customJson);
//                }

                TaskRegisterConfig.CODEC.parse(JsonOps.INSTANCE, jsonData);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            save();
        }

        public static void save() {
            File file = new File(CONFIG_FILE_PATH);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            try {
//                JsonArray jsonElements = new JsonArray();
//                for (CustomFruitHandler value : CUSTOMS.values()) {
//                    CustomFruitTemplate customFruitTemplate = value.getCustomFruitTemplate();
//                    jsonElements.add(customFruitTemplate.write());
//                }
//                JsonObject jsonData = new JsonObject();
//                jsonData.add("customs", jsonElements);

                JsonObject jsonData = TaskRegisterConfig.CODEC.encodeStart(JsonOps.INSTANCE, taskRegisterConfig)
                        .resultOrPartial(MaidsoulKitchen.LOGGER::error)
                        .map(jsonElement -> jsonElement.getAsJsonObject())
                        .orElse(null);

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(new Gson().toJson(jsonData));
                fileWriter.close();
            } catch (IOException e) {
                MaidsoulKitchen.LOGGER.warn("Could not save {} custon_fruit_handlers Configs: {}", MaidsoulKitchen.MOD_ID, e.getLocalizedMessage());
            }
        }
    }

}
