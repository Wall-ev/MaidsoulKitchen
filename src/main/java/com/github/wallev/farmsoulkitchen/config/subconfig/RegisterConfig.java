package com.github.wallev.farmsoulkitchen.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class RegisterConfig {

    public static ForgeConfigSpec.BooleanValue BERRY_FARM_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue FRUIT_FARM_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue FEED_ANIMAL_T_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue COMPAT_MELON_FARM_TASK_ENABLED;

    public static ForgeConfigSpec.BooleanValue SERENESEASONS_FARM_TASK_ENABLED;

    public static ForgeConfigSpec.BooleanValue FEED_AND_DRINK_OWNER_TASK_ENABLED;

    public static ForgeConfigSpec.BooleanValue FURNACE_TASK_ENABLED;

    public static ForgeConfigSpec.BooleanValue FD_COOK_POT_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue MD_COOK_POT_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue BNC_KEY_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue YHC_MOKA_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue YHC_TEA_KETTLE_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue YHC_DRYING_RACK_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue YHC_FERMENTATION_TANK_TASK_ENABLED;

    public static ForgeConfigSpec.BooleanValue DB_BEER_TASK_ENABLED;

    public static ForgeConfigSpec.BooleanValue DBK_COOKING_POT_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue DBK_STOVE_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue DBP_MINE_FRIDGE_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue DBP_TIKI_BAR_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue DCL_COOKING_PAN_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue DCL_COOKING_POT_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue DHB_CAULDRON_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue DHB_TEA_KETTLE_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue FERMENTATION_BARREL_TASK_ENABLED;


    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("Register");

        builder.comment("This can make the berry farm task enabled or not.");
        BERRY_FARM_TASK_ENABLED = builder.define("BerryTaskEnabled", true);
        builder.comment("This can make the fruit farm task enabled or not.");
        FRUIT_FARM_TASK_ENABLED = builder.define("FruitTaskEnabled", true);
        builder.comment("This can make the compat melon farm task enabled or not.");
        COMPAT_MELON_FARM_TASK_ENABLED = builder.define("CompatMelonFarmTaskEnabled", true);
        builder.comment("This can make the feed animal t farm task enabled or not.");
        FEED_ANIMAL_T_TASK_ENABLED = builder.define("FeedAnimalTTaskEnabled", true);

        builder.comment("This can make the sereneseasons farm task enabled or not.");
        SERENESEASONS_FARM_TASK_ENABLED = builder.define("SereneSeasonsTaskEnabled", true);

        builder.comment("This can make the feed and drink owner task enabled or not.");
        FEED_AND_DRINK_OWNER_TASK_ENABLED = builder.define("FeedAndDrinkOwnerTaskEnabled", true);

        builder.comment("This can make the furnace task enabled or not.");
        FURNACE_TASK_ENABLED = builder.define("FurnaceTaskEnabled", true);

        builder.comment("This can make the fd cook pot task enabled or not.");
        FD_COOK_POT_TASK_ENABLED = builder.define("FDCookPotTaskEnabled", true);
        builder.comment("This can make the md cook pot task enabled or not.");
        MD_COOK_POT_TASK_ENABLED = builder.define("MDCookPotTaskEnabled", true);
        builder.comment("This can make the bnc key task enabled or not.");
        BNC_KEY_TASK_ENABLED = builder.define("BNCKeyTaskEnabled", true);
        builder.comment("This can make the yhc moka task enabled or not.");
        YHC_MOKA_TASK_ENABLED = builder.define("YHC_MOKA_TaskEnabled", true);
        builder.comment("This can make the yhc tea kettle task enabled or not.");
        YHC_TEA_KETTLE_TASK_ENABLED = builder.define("YHC_TEA_KETTLE_TaskEnabled", true);
        builder.comment("This can make the yhc tea kettle task enabled or not.");
        YHC_DRYING_RACK_TASK_ENABLED = builder.define("YHC_DRYING_RACK_TaskEnabled", true);
        builder.comment("This can make the yhc fermentation tank task enabled or not.");
        YHC_FERMENTATION_TANK_TASK_ENABLED = builder.define("YHC_FERMANTATION_TANK_TaskEnabled", true);

        builder.comment("This can make the db beer task enabled or not.");
        DB_BEER_TASK_ENABLED = builder.define("DB_Beer_TaskEnabled", true);

        builder.comment("This can make the dbk cooking pot task enabled or not.");
        DBK_COOKING_POT_TASK_ENABLED = builder.define("DBK_CookingPotTaskEnabled", true);
        builder.comment("This can make the dbk stove task enabled or not.");
        DBK_STOVE_TASK_ENABLED = builder.define("DBK_StoveTaskEnabled", true);
        builder.comment("This can make the dbp mine fridge task enabled or not.");
        DBP_MINE_FRIDGE_TASK_ENABLED = builder.define("DBP_MineFridgeTaskEnabled", true);
        builder.comment("This can make the dbp tiki bar task enabled or not.");
        DBP_TIKI_BAR_TASK_ENABLED = builder.define("DBP_TikiBarTaskEnabled", true);
        builder.comment("This can make the dcl cooking pan task enabled or not.");
        DCL_COOKING_PAN_TASK_ENABLED = builder.define("DCL_CookingPanTaskEnabled", true);
        builder.comment("This can make the dcl cooking pot task enabled or not.");
        DCL_COOKING_POT_TASK_ENABLED = builder.define("DCL_CookingPotTaskEnabled", true);
        builder.comment("This can make the dhb cauldron task enabled or not.");
        DHB_CAULDRON_TASK_ENABLED = builder.define("DHB_CauldronTaskEnabled", true);
        builder.comment("This can make the dhb tea kettle task enabled or not.");
        DHB_TEA_KETTLE_TASK_ENABLED = builder.define("DHB_TeaKettleTaskEnabled", true);
        builder.comment("This can make the fermentation barrel task enabled or not.");
        FERMENTATION_BARREL_TASK_ENABLED = builder.define("FermentationBarrelTaskEnabled", true);

        builder.pop();
    }
}
