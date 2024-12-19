package com.github.wallev.maidsoulkitchen.init.registry.tlm;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.BerryData;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.FruitData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;

public final class RegisterData {
    public static TaskDataKey<BerryData> BERRY_FARM;
    public static TaskDataKey<FruitData> FRUIT_FARM;
    public static TaskDataKey<CookData> MC_FURNACE;
    // farmer's delight && addon
    public static TaskDataKey<CookData> FD_COOK_POT;
    public static TaskDataKey<CookData> FD_CUTTING_BOARD;
    public static TaskDataKey<CookData> FR_KETTLE;
    public static TaskDataKey<CookData> MD_COPPER_POT;
    public static TaskDataKey<CookData> BNC_KEY;
    public static TaskDataKey<CookData> YHC_MOKA;
    public static TaskDataKey<CookData> YHC_TEA_KETTLE;
    public static TaskDataKey<CookData> YHC_DRYING_RACK;
    public static TaskDataKey<CookData> YHC_FERMENTATION_TANK;
    public static TaskDataKey<CookData> CP_CROCK_POT;
    public static TaskDataKey<CookData> DB_BEER;
    public static TaskDataKey<CookData> KC_BREW_BARREL;
    public static TaskDataKey<CookData> KC_AIR_COMPRESSOR;
    public static TaskDataKey<CookData> DBK_COOKING_POT;
    public static TaskDataKey<CookData> DBP_MINE_FRIDGE;
    public static TaskDataKey<CookData> DBP_TIKI_BAR;
    public static TaskDataKey<CookData> DCL_COOKING_PAN;
    public static TaskDataKey<CookData> DCL_COOKING_POT;
    public static TaskDataKey<CookData> DCL_STOVE;
    public static TaskDataKey<CookData> DFC_ROAST;
    public static TaskDataKey<CookData> DFC_COOKING_POT;
    public static TaskDataKey<CookData> DFC_STOVE;
    public static TaskDataKey<CookData> DHB_CAULDRON;
    public static TaskDataKey<CookData> DHB_TEA_KETTLE;
    public static TaskDataKey<CookData> FERMENTATION_BARREL;
    private RegisterData() {
    }

    public static void register(TaskDataRegister register) {
        BERRY_FARM = register.register(TaskInfo.BERRY_FARM.uid, BerryData.CODEC);
        FRUIT_FARM = register.register(TaskInfo.FRUIT_FARM.uid, FruitData.CODEC);

        MC_FURNACE = register.register(TaskInfo.FURNACE.uid, CookData.CODEC);

        FD_COOK_POT = register.register(TaskInfo.FD_COOK_POT.uid, CookData.CODEC);
        FD_CUTTING_BOARD = register.register(TaskInfo.FD_CUTTING_BOARD.uid, CookData.CODEC);
        MD_COPPER_POT = register.register(TaskInfo.MD_COOK_POT.uid, CookData.CODEC);
        BNC_KEY = register.register(TaskInfo.BNC_KEY.uid, CookData.CODEC);
        FR_KETTLE = register.register(TaskInfo.FR_KETTLE.uid, CookData.CODEC);
        YHC_MOKA = register.register(TaskInfo.YHC_MOKA.uid, CookData.CODEC);
        YHC_TEA_KETTLE = register.register(TaskInfo.YHC_TEA_KETTLE.uid, CookData.CODEC);
        YHC_DRYING_RACK = register.register(TaskInfo.YHC_DRYING_RACK.uid, CookData.CODEC);
        YHC_FERMENTATION_TANK = register.register(TaskInfo.YHC_FERMENTATION_TANK.uid, CookData.CODEC);

        CP_CROCK_POT = register.register(TaskInfo.CP_CROCK_POT.uid, CookData.CODEC);
        DB_BEER = register.register(TaskInfo.DB_BEER.uid, CookData.CODEC);
        KC_BREW_BARREL = register.register(TaskInfo.KK_BREW_BARREL.uid, CookData.CODEC);
        KC_AIR_COMPRESSOR = register.register(TaskInfo.KK_AIR_COMPRESSOR.uid, CookData.CODEC);

        DBK_COOKING_POT = register.register(TaskInfo.DBK_COOKING_POT.uid, CookData.CODEC);
        DBP_MINE_FRIDGE = register.register(TaskInfo.DBP_MINE_FRIDGE.uid, CookData.CODEC);
        DBP_TIKI_BAR = register.register(TaskInfo.DBP_TIKI_BAR.uid, CookData.CODEC);
        DCL_COOKING_PAN = register.register(TaskInfo.DCL_COOKING_PAN.uid, CookData.CODEC);
        DCL_COOKING_POT = register.register(TaskInfo.DCL_COOKING_POT.uid, CookData.CODEC);
        DCL_STOVE = register.register(TaskInfo.DCL_STOVE.uid, CookData.CODEC);
        DFC_ROAST = register.register(TaskInfo.DFC_ROAST.uid, CookData.CODEC);
        DFC_COOKING_POT = register.register(TaskInfo.DFC_COOKING_POT.uid, CookData.CODEC);
        DFC_STOVE = register.register(TaskInfo.DFC_STOVE.uid, CookData.CODEC);
        DHB_CAULDRON = register.register(TaskInfo.DHB_CAULDRON.uid, CookData.CODEC);
        DHB_TEA_KETTLE = register.register(TaskInfo.DHB_TEA_KETTLE.uid, CookData.CODEC);
        FERMENTATION_BARREL = register.register(TaskInfo.FERMENTATION_BARREL.uid, CookData.CODEC);
    }
}