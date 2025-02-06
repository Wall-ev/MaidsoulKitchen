package com.github.wallev.maidsoulkitchen.task;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraft.resources.ResourceLocation;

public enum TaskInfo {
    BERRY_FARM("berries_farm"),
    FRUIT_FARM("fruit_farm"),
    FEED_ANIMAL_T("feed_animal_t"),
    COMPAT_MELON_FARM("compat_melon"),

    SERENESEASONS_FARM("sereneseasons_farm"),

    ECLIPTICSSEASONS_FARM("eclipticseasons_farm"),

    FEED_AND_DRINK_OWNER("feedanddrink"),

    FURNACE("furnace"),

    FD_COOK_POT("fd_cooking_pot"),
    FD_CUTTING_BOARD("fd_cutting_board"),
    CD_CUISINE_SKILLET("cd_cuisine_skillet"),
    MD_COOK_POT("md_copper_pot"),
    BNC_KEY("bnc_key"),
    FR_KETTLE("fr_kettle"),
    YHC_MOKA("yhc_moka_pot"),
    BD_BASIN("bd_basin"),
    BD_GRILL("bd_grill"),
    YHC_TEA_KETTLE("yhc_tea_kettle"),
    YHC_DRYING_RACK("yhc_drying_rack"),
    YHC_FERMENTATION_TANK("yhc_fermentation_tank"),

    KK_BREW_BARREL("kk_brew_barrel"),
    KK_AIR_COMPRESSOR("kk_air_compressor"),
    DB_BEER("drinkbeer_beerbarrel"),
    CP_CROCK_POT("cp_crock_pot"),

    DBK_COOKING_POT("dkb_cooking_pot"),
    DBP_MINE_FRIDGE("dbp_mini_fridge"),
    DBP_TIKI_BAR("dbp_tiki_bar"),
    DCL_COOKING_PAN("dcl_cooking_pan"),
    DCL_COOKING_POT("dcl_cooking_pot"),
    DCL_STOVE("dcl_stove"),
    DFC_ROAST("dfc_roast"),
    DFC_COOKING_POT("dfc_cooking_pot"),
    DFC_STOVE("dfc_stove"),
    DHB_CAULDRON("dhb_cauldron"),
    DHB_TEA_KETTLE("dhb_tea_kettle"),
    FERMENTATION_BARREL("dv_fermentation_barrel");
    public final ResourceLocation uid;

    TaskInfo(ResourceLocation uid) {
        this.uid = uid;
    }
    TaskInfo(String uid) {
        this.uid = new ResourceLocation(MaidsoulKitchen.MOD_ID, uid);
    }
}
