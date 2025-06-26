package com.github.wallev.maidsoulkitchen.init.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.BerryData;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.FruitData;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.SwappedInvData;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;

public final class DataRegister {
    public static TaskDataKey<BerryData> BERRY_FARM;
    public static TaskDataKey<FruitData> FRUIT_FARM;
    public static TaskDataKey<CookData> MC_FURNACE;
    public static TaskDataKey<CookData> FD_COOK_POT;
    public static TaskDataKey<CookData> FD_CUTTING_BOARD;
    public static TaskDataKey<CookData> FR_KETTLE;
    public static TaskDataKey<CookData> MD_COPPER_POT;
    public static TaskDataKey<CookData> BNC_KEY;
    public static TaskDataKey<CookData> BD_BASIN;
    public static TaskDataKey<CookData> BD_GRILL;
    public static TaskDataKey<CookData> YHC_MOKA;
    public static TaskDataKey<CookData> YHC_TEA_KETTLE;
    public static TaskDataKey<CookData> YHC_DRYING_RACK;
    public static TaskDataKey<CookData> YHC_FERMENTATION_TANK;
    public static TaskDataKey<CookData> CUISINE_SKILLET;
    public static TaskDataKey<CookData> CP_CROCK_POT;
    public static TaskDataKey<CookData> DB_BEER;
    public static TaskDataKey<CookData> KC_BREW_BARREL;
    public static TaskDataKey<CookData> KC_AIR_COMPRESSOR;

    public static TaskDataKey<CookData> KC_POT;
    public static TaskDataKey<CookData> KC_CHOPPING_BOARD;

    public static TaskDataKey<SwappedInvData> SWAPPED_INV;
    private DataRegister() {
    }

    public static void init(TaskDataRegister data) {
        BERRY_FARM = data.register(MaidsoulKitchenTask.BERRY_FARM.uid, BerryData.CODEC);
        FRUIT_FARM = data.register(MaidsoulKitchenTask.FRUIT_FARM.uid, FruitData.CODEC);

        MC_FURNACE = data.register(MaidsoulKitchenTask.FURNACE.uid, CookData.CODEC);

        FD_COOK_POT = data.register(MaidsoulKitchenTask.FD_COOK_POT.uid, CookData.CODEC);
        FD_CUTTING_BOARD = data.register(MaidsoulKitchenTask.FD_CUTTING_BOARD.uid, CookData.CODEC);
        CUISINE_SKILLET = data.register(MaidsoulKitchenTask.CD_CUISINE_SKILLET.uid, CookData.CODEC);
        MD_COPPER_POT = data.register(MaidsoulKitchenTask.MD_COOK_POT.uid, CookData.CODEC);
        BNC_KEY = data.register(MaidsoulKitchenTask.BNC_KEY.uid, CookData.CODEC);
        FR_KETTLE = data.register(MaidsoulKitchenTask.FR_KETTLE.uid, CookData.CODEC);
        BD_BASIN = data.register(MaidsoulKitchenTask.BD_BASIN.uid, CookData.CODEC);
        BD_GRILL = data.register(MaidsoulKitchenTask.BD_GRILL.uid, CookData.CODEC);
        YHC_MOKA = data.register(MaidsoulKitchenTask.YHC_MOKA.uid, CookData.CODEC);
        YHC_TEA_KETTLE = data.register(MaidsoulKitchenTask.YHC_TEA_KETTLE.uid, CookData.CODEC);
        YHC_DRYING_RACK = data.register(MaidsoulKitchenTask.YHC_DRYING_RACK.uid, CookData.CODEC);
        YHC_FERMENTATION_TANK = data.register(MaidsoulKitchenTask.YHC_FERMENTATION_TANK.uid, CookData.CODEC);

        CP_CROCK_POT = data.register(MaidsoulKitchenTask.CP_CROCK_POT.uid, CookData.CODEC);
        DB_BEER = data.register(MaidsoulKitchenTask.DB_BEER.uid, CookData.CODEC);
        KC_BREW_BARREL = data.register(MaidsoulKitchenTask.KK_BREW_BARREL.uid, CookData.CODEC);
        KC_AIR_COMPRESSOR = data.register(MaidsoulKitchenTask.KK_AIR_COMPRESSOR.uid, CookData.CODEC);

        KC_POT = data.register(MaidsoulKitchenTask.KC_POT.uid, CookData.CODEC);
        KC_CHOPPING_BOARD = data.register(MaidsoulKitchenTask.KC_CHOPPING_BOARD.uid, CookData.CODEC);


        SWAPPED_INV = data.register(SwappedInvData.KEY, SwappedInvData.CODEC);
    }
}