package com.github.wallev.maidsoulkitchen.mixinmanager.legacy;

import com.electronwill.nightconfig.core.file.FileWatcher;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.mixinmanager.legacy.config.IssueCommentConfig;
import com.github.wallev.maidsoulkitchen.mixinmanager.legacy.config.TaskConfigConfig;
import com.github.wallev.maidsoulkitchen.mixinmanager.legacy.config.TaskRegisterConfig;
import com.github.wallev.maidsoulkitchen.mixinmanager.legacy.manager.TaskMixinRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

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
        putMixin("youkaishomecoming.BasePotBlockEntityMixin", true, Mods.YHCD_223_250);
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
        private static final Logger LOGGER = LogUtils.getLogger();
        static final Marker CONFIG = MarkerFactory.getMarker("CONFIG");


        private static final Map<String, TaskConfigConfig> map = new HashMap<>();
        public static final TaskRegisterConfig taskRegisterConfig = TaskMixinRegister.create();


        private static final String FILE_NAME = "custom_fruit_handlers.json";
        private static final String CONFIG_FILE_PATH = String.format("./%s/%s/%s", "config", MaidsoulKitchen.MOD_ID, FILE_NAME);

        public static void init() {
        }

        public static Map<String, TaskConfigConfig> getTaskMapConfig() {
            return map;
        }

        public static void defaultLoad() {
            setTaskVal("BerryTaskEnabled", true, "This can make the berry farm task enabled or not.");
            setTaskVal("FruitTaskEnabled", true, "This can make the fruit farm task enabled or not.");
            setTaskVal("CompatMelonFarmTaskEnabled", true, "This can make the compat melon farm task enabled or not.");
            setTaskVal("FeedAnimalTTaskEnabled", true, "This can make the feed animal t farm task enabled or not.");
            setTaskVal("SereneSeasonsTaskEnabled", true, "This can make the sereneseasons farm task enabled or not.");
            setTaskVal("EclipticSeasonsTaskEnabled", true, "This can make the eclipticseasons farm task enabled or not.");
            setTaskVal("FeedAndDrinkOwnerTaskEnabled", true, "This can make the feed and drink owner task enabled or not.");

            taskRegisterConfig.setTaskMapConfig(map);
        }

        private static void setTaskVal(String key, boolean value, String enusIssueCommentConfig) {
            map.put(key, new TaskConfigConfig(new IssueCommentConfig(enusIssueCommentConfig, "", ""), value, new HashMap<>()));
        }


        public static void load() {
            File file = new File(CONFIG_FILE_PATH);
            if (!file.exists()) {
                defaultLoad();
//                save();
            }

            try {
                loadConfigFromFile(file);

//                new ConfigFileTypeHandler.ConfigWatcher(c, configData, Thread.currentThread().getContextClassLoader())
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                FileWatcher.defaultInstance().addWatch(file, () -> {
                    // Force the regular classloader onto the special thread
                    Thread.currentThread().setContextClassLoader(contextClassLoader);

                    save();
                    try {
                        loadConfigFromFile(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
//                    load();
                    LOGGER.debug(CONFIG, "MaidsoulKitchen Config file {} changed, sending notifies", file.getName());
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            save();
        }

        private static void loadConfigFromFile(File file) throws IOException {
            String json = Files.readString(file.toPath());
            JsonObject jsonData = JsonParser.parseString(json).getAsJsonObject();
//                JsonArray customsJson = jsonData.getAsJsonArray("customs");
//                for (JsonElement jsonElement : customsJson.asList()) {
//                    JsonObject customJson = jsonElement.getAsJsonObject();
//                    CustomFruitTemplate.read(customJson);
//                }

            TaskRegisterConfig.CODEC.parse(JsonOps.INSTANCE, jsonData)
                    .resultOrPartial((s) -> MaidsoulKitchen.LOGGER.error(s))
                    .ifPresent(taskRegisterConfig -> {
                        map.clear();
                         map.putAll(taskRegisterConfig.getTaskMapConfig());
                    });
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
