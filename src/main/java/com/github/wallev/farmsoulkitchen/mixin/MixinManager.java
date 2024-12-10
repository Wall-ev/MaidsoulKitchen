package com.github.wallev.farmsoulkitchen.mixin;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MixinManager {
    private static final String MIXIN_PACKAGE = "com.github.wallev.farmsoulkitchen.mixin";
    private static final String FILE_NAME = "farmsoulkitchen-mixins.json";
    private static final String CONFIG_FILE_PATH = String.format("./%s/%s", "config", FILE_NAME);

    public static Map<String, Boolean> mixinList = new HashMap<>();
//    public static Map<String, Boolean> mixinMods = new HashMap<>();

    private static void putMixin(String mixinClass, boolean enabled) {
        mixinList.put(mixinClass, enabled);
//        mixinMods.put(mixinClass, bindMod.isLoaded);
    }

    static {
        putMixin("bakery.MixinSmallCookingPotBlockEntity", true);
        putMixin("beachparty.MixinMiniFridgeBlockEntity", true);
        putMixin("beachparty.MixinTikiBarBlockEntity", true);
        putMixin("candlelight.MixinCookingPanBlockEntity", true);
        putMixin("candlelight.MixinLargeCookingPotBlockEntity", true);
        putMixin("candlelight.MixinStoveBlockEntity", true);
        putMixin("farmacharm.MixinCookingPotBlockEntity", true);
        putMixin("farmacharm.MixinRoastBlockEntity", true);
        putMixin("farmacharm.MixinStoveBlockEntity", true);
        putMixin("herbal.MixinCauldronBlockEntity", true);
        putMixin("herbal.MixinTeaKettleBlockEntity", true);
        putMixin("vinery.MixinFermentationBarrelBlockEntity", true);

        putMixin("fd.MixinCookingPotBlockEntity", true);
        putMixin("md.MixinCopperPotBlockEntity", true);
        putMixin("bnc.KegBlockEntityAccessor", true);
        putMixin("fr.MixinKettleBlockEntity", true);
        putMixin("yhc.KettleBlockAccessor", true);
        putMixin("yhc.MixinBasePotBlockEntity", true);

        putMixin("mc.AbstractFurnaceAccessor", true);
        putMixin("kitchkarrot.AirCompressorBlockEntityAccessor", true);
        putMixin("drinkbeer.BeerBarrelBlockAccessor", true);

//        putMixin("tlm.MixinAbstractMaidContainerGui", true);
//        putMixin("tlm.MixinEntityMaid", true);
    }

    protected static boolean isMixinEnabled(String mixinClassName) {
        String mixinClassName1 = mixinClassName.replace(String.format("%s.", MIXIN_PACKAGE), "");
        return mixinList.getOrDefault(mixinClassName1, true);
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
                mixinList.replaceAll(existMixinList::getOrDefault);
                fileReader.close();
            } catch (Exception e) {
                FarmsoulKitchen.LOGGER.warn(String.format("Could not load %s Mixin Configs, creating new config. ERROR: %s", FarmsoulKitchen.MOD_ID, e.getLocalizedMessage()));
                saveMixinSettings();
            }
        } else {
            FarmsoulKitchen.LOGGER.warn(String.format("%s Mixin Configs not found, creating new config.", FarmsoulKitchen.MOD_ID));

            saveMixinSettings();
        }
    }

    protected static void saveMixinSettings() {
        Gson gson = new Gson();
        File file = new File(CONFIG_FILE_PATH);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(mixinList));
            fileWriter.close();
        } catch (IOException e) {
            FarmsoulKitchen.LOGGER.warn(String.format("Could not save %s Mixin Configs: %s", FarmsoulKitchen.MOD_ID, e.getLocalizedMessage()));
        }
    }

}
