package com.github.wallev.maidsoulkitchen.modclazzchecker.manager;

import com.github.wallev.maidsoulkitchen.config.subconfig.TaskRegisterConfig;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.util.EnumCodecUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.type.*;
import com.github.wallev.maidsoulkitchen.util.DevUtil;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("Convert2MethodRef")
public enum TaskInfo implements ITaskInfo<Mods> {
    /**
     * 没人有何实质作用，只是给{@link TaskClassAnalyzer}做默认值使用（骗过编译器x）
     */
    @IgnoreSolver
    @TaskErrorLang(en_us = "None", zh_cn = "闲置(烹饪)")
    NONE("",
            Mods.MC),

    /**
     * 默认烹饪任务，什么都不做，只给烹饪找不到任务时使用
     */
    @IgnoreSolver
    @TaskErrorLang(en_us = "Idle", zh_cn = "闲置(烹饪)")
    IDLE("idle",
            Mods.MC),
    @IgnoreSolver
    @TaskErrorLang(en_us = "Cook", zh_cn = "烹饪")
    COOK("cook",
            Mods.MC),

    @IgnoreSolver
    @TaskErrorLang(en_us = "Compat Menlon", zh_cn = "瓜类兼容")
    COMPAT_MELON_FARM("compat_melon",
            Mods.MC),
    @IgnoreSolver
    @TaskErrorLang(en_us = "Berry Farm", zh_cn = "浆果农场")
    BERRY_FARM("berries_farm",
            Mods.MC),
    @IgnoreSolver
    @TaskErrorLang(en_us = "Fruit Farm", zh_cn = "水果采摘")
    FRUIT_FARM("fruit_farm",
            Mods.MC),

    @IgnoreSolver
    @TaskErrorLang(en_us = "Feed Animal T", zh_cn = "繁殖动物2")
    FEED_ANIMAL_T("feed_animal_t",
            Mods.MC),

    @FarmModule
    @TaskErrorLang(en_us = "Serene Seasons Farm", zh_cn = "静谧四季农场")
    SERENESEASONS_FARM("sereneseasons_farm",
            Mods.SS),

    @FarmModule
    @TaskErrorLang(en_us = "Ecliptic Seasons Farm", zh_cn = "节气农场")
    ECLIPTICSSEASONS_FARM("eclipticseasons_farm",
            Mods.ES),

    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Furnace", zh_cn = "熔炉(烹饪)")
    FURNACE("furnace",
            "furnace",
            "smelting",
            Mods.MC,
            true),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Kc Pot", zh_cn = "森罗厨房 炒锅(烹饪)")
    @VersionModule(legacy0 = "kc_pot")
    KC_POT("kc_pot",
            "pot",
            "pot",
            Mods.KC,
            true,
            () -> DevUtil.isDevEnv()),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Kc Chopping Board", zh_cn = "森罗厨房 彩板(烹饪)")
    KC_CHOPPING_BOARD("kc_chopping_board",
            "chopping_board",
            "chopping_board",
            Mods.KC,
            true,
            () -> DevUtil.isDevEnv()),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Fd Cooking Pot", zh_cn = "农夫乐事 厨锅(烹饪)")
    FD_COOK_POT("fd_cooking_pot",
            "cooking_pot",
            "cooking",
            Mods.FD,
            true),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Fd Cutting Board", zh_cn = "农夫乐事 砧板(烹饪)")
    FD_CUTTING_BOARD("fd_cutting_board",
            "cutting_board",
            "cutting",
            Mods.FD,
            true,
            () -> false),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Cd Cuisine Skillet", zh_cn = "料理乐事 炒锅(烹饪)")
    CD_CUISINE_SKILLET("cd_cuisine_skillet",
            "cuisine_skillet",
            "cuisine",
            Mods.CD,
            true,
            () -> DevUtil.isDevEnv()),

    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Miners Delight CopperPot", zh_cn = "矿工乐事 铜锅(烹饪)")
    MD_COOK_POT("md_copper_pot",
            "copper_pot",
            "cooking",
            Mods.MD,
            true),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "CopperPot CopperPot", zh_cn = "CopperPot CopperPot(烹饪)")
    COPPER_POT("copper_pot",
            "copper_pot",
            "cooking",
            Mods.COPPER_POT,
            true,
            () -> DevUtil.isDevEnv()),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Dungeons Delight Cooking Pot", zh_cn = "地牢乐事 怪物锅(烹饪)")
    MONSTER_POT("monster_pot",
            "monster_pot",
            "monster_cooking",
            Mods.DUNGEONS_DELIGHT,
            true,
            () -> DevUtil.isDevEnv()),

    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Bnc Keg", zh_cn = "饮酒作乐 发酵桶(烹饪)")
    BNC_KEY("bnc_key",
            "keg",
            "fermenting",
            Mods.BNCD,
            true),

    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Fr Kettle", zh_cn = "农夫暇事 茶壶(烹饪)")
    FR_KETTLE("fr_kettle",
            "kettle",
            "brewing",
            Mods.FRD,
            true),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Bd Basin", zh_cn = "烧烤乐事 (烹饪)")
    BD_BASIN("bd_basin",
            "basin",
            "skewering",
            Mods.BD,
            true,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Bd Basin", zh_cn = "烧烤乐事 (烹饪)")
    BD_GRILL("bd_grill",
            "grill",
            "grilling",
            Mods.BD,
            true,
            () -> DevUtil.isDevEnv()),

    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Youkaishomecoming Moka Pot", zh_cn = "妖怪归家 摩卡壶(烹饪)")
    YHC_MOKA("yhc_moka_pot",
            "moka_pot",
            "moka_pot",
            Mods.YHCD_223_250,
            true,
            Mods.YHCF),
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Youkaishomecoming Kettle", zh_cn = "妖怪归家 水壶(烹饪)")
    YHC_TEA_KETTLE("yhc_tea_kettle",
            "kettle",
            "kettle",
            Mods.YHCD_223_250,
            true,
            Mods.YHCF),
    YHC_TEA_KETTLE_LEGACY(YHC_TEA_KETTLE, Mods.YHCD_223_250),
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "YoukaisHomeComing Dring Rack", zh_cn = "妖怪归家 晒干架(烹饪)")
    YHC_DRYING_RACK("yhc_drying_rack",
            "drying_rack",
            "drying_rack",
            Mods.YHCD_223_250,
            true,
            Mods.YHCF),
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "YoukaisHomeComing Ferment", zh_cn = "妖怪归家 发酵桶(烹饪)")
    YHC_FERMENTATION_TANK("yhc_fermentation_tank",
            "fermentation_tank",
            "fermentation",
            Mods.YHCD_NEW,
            true,
            Mods.YHCF),
    YHC_FERMENTATION_TANK_LEGACY(TaskInfo.YHC_FERMENTATION_TANK, Mods.YHCD_LEGACY),

    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "KitchenKarrot Brewing", zh_cn = "胡萝卜厨房 发酵桶(烹饪)")
    KK_BREW_BARREL("kk_brew_barrel",
            "brewing_barrel",
            "brewing_barrel",
            Mods.KK_NEW,
            true),
    KK_BREW_BARREL_LEGACY(TaskInfo.KK_BREW_BARREL, Mods.KK_LEGACY),
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "KitchenKarrot AirCompressor", zh_cn = "胡萝卜厨房 空气压缩机(烹饪)")
    KK_AIR_COMPRESSOR("kk_air_compressor",
            "air_compressor",
            "air_compressing",
            Mods.KK,
            true),

    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "Drink Beer Beer Barrel", zh_cn = "和啤酒啦 啤酒桶(烹饪)")
    DB_BEER("drinkbeer_beerbarrel",
            "beer_barrel",
            "brewing",
            Mods.DB,
            true),

    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    @TaskErrorLang(en_us = "CrockPot CrockPot", zh_cn = "烹饪锅 烹饪锅(烹饪)")
    CP_CROCK_POT("cp_crock_pot",
            "crock_pot",
            "crock_pot_cooking",
            Mods.CP,
            true),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
//    @TaskErrorLang(en_us = "CrockPot CrockPot", zh_cn = "烹饪锅 烹饪锅(烹饪)")
    DFC_STOVE("stove",
            Mods.DFC,
            true,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    DFC_ROASTER("roaster",
            Mods.DFC,
            true,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    DFC_COOKING_POT("cooking_pot",
            Mods.DFC,
            true,
            () -> DevUtil.isDevEnv()),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    DBK_COOKING_POT("cooking_pot",
            Mods.DBK,
            true,
            () -> DevUtil.isDevEnv()),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    DCL_COOKING_POT("cooking_pot",
            Mods.DCL,
            true,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    DCL_COOKING_PAN("cooking_pan",
            Mods.DCL,
            true,
            () -> DevUtil.isDevEnv()),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    DBP_MINI_FRIDGE("mine_fridge",
            Mods.DBP,
            true,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    DBP_PALM_BAR("palm_bar",
            Mods.DBP,
            true,
            () -> DevUtil.isDevEnv()),

    @DevModule
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.KITCHEN_MODULE)
    DM_CHEESE_FORM("cheese_form",
            Mods.DM,
            true,
            () -> DevUtil.isDevEnv()),


    /**
     * 不是实质的任务，都是集成在浆果任务里，只是给{@link TaskClassAnalyzer}分析和拦截使用
     */
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.BERRY_MODULE)
    @TaskErrorLang(en_us = "Berry Minecraft", zh_cn = "浆果模式 原版")
    BERRY_MINECRAFT("berry_minecraft",
            Mods.MC,
            () -> TaskRegisterConfig.enabled(BERRY_FARM.uid)),
    @TaskModule(type = TaskModule.Type.BERRY_MODULE)
    @TaskErrorLang(en_us = "Berry L2", zh_cn = "浆果模式 L2系列")
    BERRY_L2_HARVESTER("berry_l2_harvester",
            Mods.L2_HARVESTER,
            () -> TaskRegisterConfig.enabled(BERRY_FARM.uid)),
    @TaskModule(type = TaskModule.Type.BERRY_MODULE)
    @TaskErrorLang(en_us = "Berry Farmers Respite Green Tea", zh_cn = "浆果模式 农夫暇事-绿茶")
    BERRY_FARMERS_RESPITE_GREEN_TEA("berry_farmersrespite_greentea",
            Mods.FRD,
            () -> TaskRegisterConfig.enabled(BERRY_FARM.uid)),
    @TaskModule(type = TaskModule.Type.BERRY_MODULE)
    @TaskErrorLang(en_us = "Berry Farmers Respite Yellow Tea", zh_cn = "浆果模式 农夫暇事-黄茶")
    BERRY_FARMERS_RESPITE_YELLOW_TEA("berry_farmersrespite_yellowtea",
            Mods.FRD,
            () -> TaskRegisterConfig.enabled(BERRY_FARM.uid)),
    @TaskModule(type = TaskModule.Type.BERRY_MODULE)
    @TaskErrorLang(en_us = "Berry Farmers Respite Black Tea", zh_cn = "浆果模式 农夫暇事-黑茶")
    BERRY_FARMERS_RESPITE_BLACK_TEA("berry_farmersrespite_blacktea",
            Mods.FRD,
            () -> TaskRegisterConfig.enabled(BERRY_FARM.uid)),
    @TaskModule(type = TaskModule.Type.BERRY_MODULE)
    @TaskErrorLang(en_us = "Berry Simple Farming", zh_cn = "浆果模式 简单农业")
    BERRY_SIMPLE_FARMING("berry_simple_farming",
            Mods.SF,
            () -> TaskRegisterConfig.enabled(BERRY_FARM.uid)),
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.BERRY_MODULE)
    @TaskErrorLang(en_us = "Berry Compat", zh_cn = "浆果模式 兼容")
    BERRY_COMPAT("berry_compat",
            Mods.MC,
            () -> TaskRegisterConfig.enabled(BERRY_FARM.uid)),

    /**
     * 不是实质的任务，都是集成在果树任务里，只是给{@link TaskClassAnalyzer}分析和拦截使用
     */
    @TaskModule(type = TaskModule.Type.FRUIT_MODULE)
    @TaskErrorLang(en_us = "Fruit Simple Farming", zh_cn = "果树模式 简单农业")
    FRUIT_SIMPLE_FARMING("fruit_simple_farming",
            Mods.SF,
            () -> TaskRegisterConfig.enabled(FRUIT_FARM.uid)),
    @IgnoreSolver
    @TaskModule(type = TaskModule.Type.FRUIT_MODULE)
    @TaskErrorLang(en_us = "Fruit Compat", zh_cn = "果树模式 兼容")
    FRUIT_COMPAT("fruit_compat",
            Mods.MC,
            () -> TaskRegisterConfig.enabled(FRUIT_FARM.uid)),


    /**
     * 不是实质的任务，都是旗帜渲染，只是给{@link TaskClassAnalyzer}分析和拦截使用
     */
    @DevModule
    @IgnoreSolver
    @LayerModule
    LAYER_BAKERY("layer_bakery",
            Mods.DBK,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @LayerModule
    LAYER_BEACHPARTY("layer_beachparty",
            Mods.DBP,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @LayerModule
    LAYER_BLOOMINGNATURE("layer_bloomingnature",
            Mods.DBN,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @LayerModule
    LAYER_BREWERY("layer_brewery",
            Mods.DBR,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @LayerModule
    LAYER_CANDLELIGHT("layer_candlelight",
            Mods.DCL,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @LayerModule
    LAYER_HERBALBREWS("layer_herbalbrews",
            Mods.DHB,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @LayerModule
    LAYER_MEADOW("layer_meadow",
            Mods.DM,
            () -> DevUtil.isDevEnv()),
    @DevModule
    @IgnoreSolver
    @LayerModule
    LAYER_VINERY("layer_vinery",
            Mods.DBR,
            () -> DevUtil.isDevEnv()),

    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Maid Storage Manager Core Compat(Maid Storage Manger)", zh_cn = "仓管兼容核心(女仆仓管)")
    MSM_CORE("msm_core", Mods.MAID_STORAGE_MANAGER, true),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Maid Storage Manager Single Water(Maid Storage Manger)", zh_cn = "单格水源接水(女仆仓管)")
    MSM_SINGLE_GETTER_WATER("msm_single_getter_water", Mods.MAID_STORAGE_MANAGER, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Maid Storage Manager Multi Water(Maid Storage Manger)", zh_cn = "无限水源接水(女仆仓管)")
    MSM_MULTI_GETTER_WATER("msm_erea_getter_water", Mods.MAID_STORAGE_MANAGER, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Item Use(Maid Storage Manger)", zh_cn = "物品使用(女仆仓管)")
    MSM_ITEM_USE("msm_item_use", Mods.MAID_STORAGE_MANAGER, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Farmers Delight Hand Skillet(Maid Storage Manger)", zh_cn = "农夫乐事 手持煎锅(女仆仓管)")
    MSM_FD_HAND_SKILLET("msm_fd_hand_skillet", Mods.FD, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Farmers Delight Skillet(Maid Storage Manger)", zh_cn = "农夫乐事 煎锅(女仆仓管)")
    MSM_FD_SKILLET("msm_fd_skillet", Mods.FD, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Farmers Delight Stove(Maid Storage Manger)", zh_cn = "农夫乐事 炉灶(女仆仓管)")
    MSM_FD_STOVE("msm_fd_stove", Mods.FD, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Farmers Delight Cooking Pot(Maid Storage Manger)", zh_cn = "农夫乐事 厨锅(女仆仓管)")
    MSM_FD_COOKING_POT("msm_fd_cooking_pot", Mods.FD, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Farmers Delight Cutting Board(Maid Storage Manger)", zh_cn = "农夫乐事 砧板(女仆仓管)")
    MSM_FD_CUTTING_BOARD("msm_fd_cutting_board", Mods.FD, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Minders Delight Copper Pot(Maid Storage Manger)", zh_cn = "旷工乐事 厨锅(女仆仓管)")
    MSM_MD_COOKING_POT("msm_md_cooking_pot", Mods.MD, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Dungeons Delight Cooking Pot(Maid Storage Manger)", zh_cn = "地牢乐事 怪物锅(女仆仓管)")
    MSM_DD_MONSTER_POT("msm_dd_monster_pot", Mods.DUNGEONS_DELIGHT, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Copperpot Copper Pot(Maid Storage Manger)", zh_cn = "Copperpot Copper Pot(女仆仓管)")
    MSM_CPD_COPPER_POT("msm_cp_copper_pot", Mods.COPPER_POT, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Drink Beer Mixed Beer(Maid Storage Manger)", zh_cn = "和啤酒啦 调酒(女仆仓管)")
    MSM_DB_MIXED_BEER("msm_db_mixed_beer", Mods.DB, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Drink Beer Beer Barrel(Maid Storage Manger)", zh_cn = "和啤酒啦 啤酒桶(女仆仓管)")
    MSM_DB_DRINKBEER_BEERBARREL("msm_db_drinkbeer_beerbarrel", Mods.DB, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "YoukaisHomeComing Cuinse(Maid Storage Manger)", zh_cn = "妖怪归家 料理台(女仆仓管)")
    MSM_YHC_CUISINE("msm_yhc_cuinse", Mods.YHCD, true, Mods.YHCF, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "YoukaisHomeComing Basin(Maid Storage Manger)", zh_cn = "妖怪归家 木盆(女仆仓管)")
    MSM_YHC_BASIN("msm_yhc_basin", Mods.YHCD, true, Mods.YHCF, MSM_CORE),
//    MSM_YHCF_BASIN(MSM_YHC_BASIN, Mods.YHCF),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "YoukaisHomeComing Ferment(Maid Storage Manger)", zh_cn = "妖怪归家 发酵桶(女仆仓管)")
    MSM_YHC_FERMENT("msm_yhc_ferment", Mods.YHCD_NEW, true, Mods.YHCF, MSM_CORE),
//    MSM_YHCF_FERMENT(MSM_YHC_FERMENT, Mods.YHCF),
    MSM_YHC_FERMENT_LEGACY(MSM_YHC_FERMENT, Mods.YHCD_223_250),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "YoukaisHomeComing Dring Rack(Maid Storage Manger)", zh_cn = "妖怪归家 晒干架(女仆仓管)")
    MSM_YHC_DRYING_RACK("msm_yhc_drying_rack", Mods.YHCD, true, Mods.YHCF, MSM_CORE),
//    MSM_YHCF_DRYING_RACK(MSM_YHC_DRYING_RACK, Mods.YHCF),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "YoukaisHomeComing Moka Pot(Maid Storage Manger)", zh_cn = "妖怪归家 摩卡锅(女仆仓管)")
    MSM_YHC_MOKA_POT("msm_yhc_moka_pot", Mods.YHCD, true, Mods.YHCF, MSM_CORE),
//    MSM_YHCF_MOKA_POT(MSM_YHC_MOKA_POT, Mods.YHCF),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "YoukaisHomeComing Kettle(Maid Storage Manger)", zh_cn = "妖怪归家 水壶(女仆仓管)")
    MSM_YHC_KETTLE("msm_yhc_kettle", Mods.YHCD, true, Mods.YHCF, MSM_CORE),
//    MSM_YHCF_KETTLE(MSM_YHC_KETTLE, Mods.YHCF),
    MSM_YHC_KETTLE_LEGACY(MSM_YHC_KETTLE, Mods.YHCD_223_250),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Tea Aroma Bamboo Tray(Maid Storage Manger)", zh_cn = "茶香 竹托盘(女仆仓管)")
    MSM_TA_BAMBOO_TRAY("msm_ta_bamboo_tray", Mods.TA, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Tea Aroma Brewing(Maid Storage Manger)", zh_cn = "茶香 沏茶(女仆仓管)")
    MSM_TA_BREWING("msm_ta_brewing", Mods.TA, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Tea Aroma Foam(Maid Storage Manger)", zh_cn = "茶香 加奶泡(女仆仓管)")
    MSM_TA_FOAM("msm_ta_foam", Mods.TA, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Tea Aroma Boiling(Maid Storage Manger)", zh_cn = "茶香 烧水/热牛奶(女仆仓管)")
    MSM_TA_BOILING("msm_ta_boiling", Mods.TA, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Tea Aroma Kettle Water(Maid Storage Manger)", zh_cn = "茶香 接水(女仆仓管)")
    MSM_TA_KETTLE_WATER("msm_ta_kettle_water", Mods.TA, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "KitchenKarrot Plate Cutting(Maid Storage Manger)", zh_cn = "胡萝卜厨房 窃夺(女仆仓管)")
    MSM_KK_PLATEC_CUTTING("msm_kc_plate_cutting", Mods.KK, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "KitchenKarrot Brewing(Maid Storage Manger)", zh_cn = "胡萝卜厨房 发酵桶(女仆仓管)")
    MSM_KK_BREWING("msm_kk_brewing", Mods.KK, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "KitchenKarrot AirCompressor(Maid Storage Manger)", zh_cn = "胡萝卜厨房 空气压缩机(女仆仓管)")
    MSM_KK_AIR_COMPRESSOR("msm_kk_air_compressor", Mods.KK, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Immortalers Enchantal Cooler(Maid Storage Manger)", zh_cn = "千古乐事 冷凝器(女仆仓管)")
    MSM_IMD_ENCHANTAL_COOLER("msm_imd_enchantal_cooler", Mods.IMD, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Vintage Delight Fermenting Jar(Maid Storage Manger)", zh_cn = "腌制乐事 发酵罐(女仆仓管)")
    MSM_VTD_FERMENTING_JAR("msm_vtd_fermenting_jar", Mods.VTD, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Bakeries Drink Cup(Maid Storage Manger)", zh_cn = "烘焙坊 饮料杯(女仆仓管)")
    MSM_BAKERY_DRINK_CUP("msm_bakery_drink_cup", Mods.BAKERIES, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Bakeries Cake Roll(Maid Storage Manger)", zh_cn = "烘焙坊 蛋糕卷(女仆仓管)")
    MSM_BAKERIES_CAKE_ROLL("msm_bakeries_cake_roll", Mods.BAKERIES, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Bakery Small Cooking Pot(Maid Storage Manger)", zh_cn = "馥郁烘焙 小烹饪锅(女仆仓管)")
    MSM_DO_BEKERY_SMALL_COOKING_POT("msm_do_bakery_small_cooking_pot", Mods.DBK, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Bakeris Blender(Maid Storage Manger)", zh_cn = "烘焙坊 搅拌机(女仆仓管)")
    MSM_BAKERIES_BLENDER("msm_bakeris_blender", Mods.BAKERIES, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Bakeris Oven(Maid Storage Manger)", zh_cn = "烘焙坊 烤炉(女仆仓管)")
    MSM_BAKERIES_OVEN("msm_bakeries_oven", Mods.BAKERIES, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Bakeris Dough(Maid Storage Manger)", zh_cn = "烘焙坊 面胚制作台(女仆仓管)")
    MSM_BAKERIES_DOUGH("msm_bakeries_dough", Mods.BAKERIES, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Bakeris Fermentation Tank(Maid Storage Manger)", zh_cn = "烘焙坊 发酵罐(女仆仓管)")
    MSM_BAKERIES_FERMENTATION_TANK("msm_bakeries_fermentation_tank", Mods.BAKERIES, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Beach Part Mine Fridge(Maid Storage Manger)", zh_cn = "沙滩派对 小冰箱(女仆仓管)")
    MSM_DO_BEACH_PARTY_MINI_FRIDGE("msm_do_beach_party_mini_frige", Mods.DBP, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Beach Part Plam Bar(Maid Storage Manger)", zh_cn = "沙滩派对 提基吧台(女仆仓管)")
    MSM_BEARCH_PARTY_PALM_BAR("msm_do_beach_party_plam_bar", Mods.DBP, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Candle Light Cooking Pot(Maid Storage Manger)", zh_cn = "烛火晚宴 汤锅(女仆仓管)")
    MSM_CANDLE_LINGHT_COOKING_POT("msm_candle_light_cooking_pot", Mods.DCL, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Candle Light Roase(Maid Storage Manger)", zh_cn = "烛火晚宴 平底锅(女仆仓管)")
    MSM_CANDLE_LINGHT_ROAST("msm_candle_light_roast", Mods.DCL, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Farm And Charm Cooking Pot(Maid Storage Manger)", zh_cn = "沉浸农艺 烹饪锅(女仆仓管)")
    MSM_FARM_AND_CHARM_COOKING_POT("msm_farm_and_charm_cooking_pot", Mods.DFC, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Farm And Charm Crafting Bowl(Maid Storage Manger)", zh_cn = "沉浸农艺 混合碗(女仆仓管)")
    MSM_FARM_AND_CHARM_CRAFTING_BOWL("msm_farm_and_charm_crafting_bowl", Mods.DFC, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Farm And Charm Roster(Maid Storage Manger)", zh_cn = "沉浸农艺 烘焙机(女仆仓管)")
    MSM_FARM_AND_CHARM_ROASTER("msm_farm_and_charm_roster", Mods.DFC, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Farm And Charm Stove(Maid Storage Manger)", zh_cn = "沉浸农艺 火炉(女仆仓管)")
    MSM_FARM_AND_CHARM_STOVE("msm_farm_and_charm_stove", Mods.DFC, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Kaleidoscope Cookery Cooker(Maid Storage Manger)", zh_cn = "森罗厨房 炒锅(女仆仓管)")
    MSM_KC_COOKER("msm_kc_cooker", Mods.KC, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Kaleidoscope Cookery Stock Pot(Maid Storage Manger)", zh_cn = "森罗厨房 汤锅(女仆仓管)")
    MSM_KC_STOCK_POT("msm_kc_stock_pot", Mods.KC, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Kaleidoscope Cookery Chopping Board(Maid Storage Manger)", zh_cn = "森罗厨房 彩板(女仆仓管)")
    MSM_KC_CHOPPING_BOARD("msm_kc_cutting_board", Mods.KC, true, MSM_CORE),
    @IgnoreSolver
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Kitchen Karrot Shaker(Maid Storage Manger)", zh_cn = "胡萝卜厨房 摇酒壶(女仆仓管)")
    MSM_KK_SHAKER("msm_kk_shaker", Mods.KK, true, () -> false, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Meadow Cheese Form(Maid Storage Manger)", zh_cn = "Meadow 奶酪模具(女仆仓管)")
    MSM_MEADOW_CHEESE_FORM("msm_meadow_cheese_form", Mods.DM, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Simple Farming Fermenter(Maid Storage Manger)", zh_cn = "简单农业 Fermenter(女仆仓管)")
    MSM_SIMPLE_FARMING_FERMENTER("msm_simple_farming_ferment", Mods.SF, true, MSM_CORE),
    @MaidStorageManagerCompatModule
    @TaskErrorLang(en_us = "Do Vinery Basin(Maid Storage Manger)", zh_cn = "葡园酒香 葡萄藤盆女仆仓管)")
    MSM_VINERY_GRAPE_POT("msm_vinery_grape_pot", Mods.DV, true, MSM_CORE),

    @IgnoreSolver
    PACK_LITMIT("pack_limit", Mods.TLM, () -> {
        return DevUtil.isDevEnv();
    }),
    ;
    public static final TaskInfo[] VALUES = values();
    public static final Codec<TaskInfo> CODEC = EnumCodecUtil.fromEnum(() -> VALUES);

    private String oldName = "";
    private final ResourceLocation uid;
    private final Mods bindMod;
    @Nullable
    private final Mods secondMod;
    private final Supplier<Boolean> bindConfig;
    private final List<TaskInfo> dependTasks;
    private final List<Mods> dependMods;

    // -----------------------------------------------------------TaskInfo----------------------------------------------------------------//
    TaskInfo(ResourceLocation uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, TaskInfo... dependTasks) {
        this.uid = uid;
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.get().get();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = null;
    }

    TaskInfo(ResourceLocation uid, Mods bindMod, TaskInfo... dependTasks) {
        this.uid = uid;
        this.bindMod = bindMod;
        this.bindConfig = () -> TaskRegisterConfig.enabled(this.uid);
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = null;
    }

    TaskInfo(ResourceLocation uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this.uid = uid;
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.get().get();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = secondMod;
    }

    TaskInfo(ResourceLocation uid, Mods bindMod, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this.uid = uid;
        this.bindMod = bindMod;
        this.bindConfig = () -> TaskRegisterConfig.enabled(this.uid);
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = secondMod;
    }

    TaskInfo(String oldName, String uid, Mods bindMod, boolean concatModId, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.get().get();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = null;
    }

    TaskInfo(String oldName, String uid, Mods bindMod, boolean concatModId, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> TaskRegisterConfig.enabled(this.uid);
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = null;
    }

    TaskInfo(String oldName, String uid, Mods bindMod, boolean concatModId, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.get().get();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = secondMod;
    }

    TaskInfo(String oldName, String uid, Mods bindMod, boolean concatModId, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> TaskRegisterConfig.enabled(this.uid);
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = secondMod;
    }

    TaskInfo(String oldName, String uid, Mods bindMod, boolean concatModId, Config bindConfig, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.canLoad();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = null;
    }

    TaskInfo(String oldName, String uid, Mods bindMod, boolean concatModId, Config bindConfig, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.canLoad();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = secondMod;
    }

    TaskInfo(String oldName, String blockId, String recipeId, Mods bindMod, boolean concatModId, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, blockId + "_" + recipeId, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.get().get();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = null;
    }

    TaskInfo(String oldName, String blockId, String recipeId, Mods bindMod, boolean concatModId, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, blockId + "_" + recipeId, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> TaskRegisterConfig.enabled(this.uid);
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = null;
    }

    TaskInfo(String oldName, String blockId, String recipeId, Mods bindMod, boolean concatModId, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, blockId + "_" + recipeId, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.get().get();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = secondMod;
    }

    TaskInfo(String oldName, String blockId, String recipeId, Mods bindMod, boolean concatModId, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, blockId + "_" + recipeId, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> TaskRegisterConfig.enabled(this.uid);
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = secondMod;
    }

    TaskInfo(String oldName, String blockId, String recipeId, Mods bindMod, boolean concatModId, Config bindConfig, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, blockId + "_" + recipeId, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.canLoad();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = null;
    }

    TaskInfo(String oldName, String blockId, String recipeId, Mods bindMod, boolean concatModId, Config bindConfig, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this.oldName = VResourceLocation.ofMod(oldName).toString();
        this.uid = convertUid(oldName, blockId + "_" + recipeId, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.canLoad();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = secondMod;
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, TaskInfo... dependTasks) {
        this("", uid, bindMod, concatModId, bindConfig, dependTasks);
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this("", uid, bindMod, concatModId, bindConfig, secondMod, dependTasks);
    }

    TaskInfo(String uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, TaskInfo... dependTasks) {
        this(uid, bindMod, false, bindConfig, dependTasks);
    }

    TaskInfo(String uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this(uid, bindMod, false, bindConfig, secondMod, dependTasks);
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId, Config bindConfig, TaskInfo... dependTasks) {
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.canLoad();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = null;
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId, Config bindConfig, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> bindConfig.canLoad();
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = secondMod;
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId, TaskInfo... dependTasks) {
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> true;
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = null;
    }

    TaskInfo(String uid, Mods bindMod, boolean concatModId, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this.uid = convertUid(oldName, uid, bindMod, concatModId);
        this.bindMod = bindMod;
        this.bindConfig = () -> true;
        this.dependTasks = List.of(dependTasks);
        this.dependMods = List.of();
        this.secondMod = secondMod;
    }

    TaskInfo(String uid, Mods bindMod, Config bindConfig, TaskInfo... dependTasks) {
        this(uid, bindMod, false, bindConfig, dependTasks);
    }


    TaskInfo(String uid, Mods bindMod, Config bindConfig, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this(uid, bindMod, false, bindConfig, secondMod, dependTasks);
    }

    TaskInfo(String uid, Mods bindMod, TaskInfo... dependTasks) {
        this(uid, bindMod, false, dependTasks);
    }

    TaskInfo(String uid, Mods bindMod, @Nullable Mods secondMod, TaskInfo... dependTasks) {
        this(uid, bindMod, false, secondMod, dependTasks);
    }

    /**
     * 专为 LegacyTask 使用
     * @param parentTaskInfo 原始的Task
     * @param legacyMod 旧版本的模组的区间
     */
    TaskInfo(TaskInfo parentTaskInfo, Mods legacyMod) {
        this.uid = parentTaskInfo.uid;
        this.bindMod = legacyMod;
        this.bindConfig = parentTaskInfo.bindConfig;
        this.dependTasks = parentTaskInfo.dependTasks;
        this.dependMods = parentTaskInfo.dependMods;
        this.secondMod = parentTaskInfo.secondMod;
    }

    // -----------------------------------------------------------TaskInfo----------------------------------------------------------------//

    public static void init() {
    }

    private static ResourceLocation convertUid(String oldName, String uid, Mods bindMod, boolean concatModId) {
//        if (oldName.isEmpty()) {
            return concatModId ? VResourceLocation.ofMod(bindMod.modId() + "_" + uid) : VResourceLocation.ofMod(uid);
//        }
//        return VResourceLocation.createMod(oldName);
    }

    public static TaskInfo by(String key) {
        return TaskInfo.valueOf(key);
    }

    @Nullable
    public static TaskInfo by(ResourceLocation taskUid) {
        for (TaskInfo value : TaskInfo.values()) {
            if (value.uid.equals(taskUid)) {
                return value;
            }
        }
        return null;
    }

    public ResourceLocation getUid() {
        return uid;
    }

    @Override
    public String getUidStr() {
        return uid.toString();
    }

    @Override
    public Mods getBindMod() {
        return bindMod;
    }

    public @Nullable Mods getSecondMod() {
        return secondMod;
    }

    public boolean modVersionLoaded() {
        return bindMod.versionLoad() || (this.secondMod != null && this.secondMod.versionLoad());
    }

    public boolean configEnabled() {
        return bindConfig.get();
    }

    public final boolean canLoad() {
        return canLoadWithoutCheckClazz() && TaskModClazzManager.clazzLoad(this.uid.toString());
    }

    @Override
    public boolean canLoadWithoutCheckClazz() {
        return modVersionLoaded() && configEnabled() && this.dependCanLoad();
    }

    public boolean dependCanLoad() {
        return this.dependTasks.stream().allMatch(TaskInfo::canLoadWithoutCheckClazz) && this.dependMods.stream().allMatch(Mods::versionLoad);
    }

    /**
     * use {@link #getUid()} instead
     */
    @Deprecated
    public String getOldName() {
        return oldName;
    }

    public boolean isDevTask() {
        return this.getDeclaringClass().isAnnotationPresent(DevModule.class);
    }

    @NotNull
    @Override
    public String getSerializedName() {
        return this.name();
//        return getUid().toString();
    }

    private interface Config {
        boolean canLoad();
    }
}
