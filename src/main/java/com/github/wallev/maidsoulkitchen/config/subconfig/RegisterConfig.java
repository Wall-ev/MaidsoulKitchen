package com.github.wallev.maidsoulkitchen.config.subconfig;

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
    public static ForgeConfigSpec.BooleanValue FD_CUTTING_BOARD_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue CD_CUISINE_SKILLET_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue MD_COOK_POT_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue BNC_KEY_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue FR_KETTLE_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue YHC_MOKA_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue YHC_TEA_KETTLE_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue YHC_DRYING_RACK_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue YHC_FERMENTATION_TANK_TASK_ENABLED;

    public static ForgeConfigSpec.BooleanValue KK_BREW_BARREL;
    public static ForgeConfigSpec.BooleanValue KK_AIR_COMPRESSOR;
    public static ForgeConfigSpec.BooleanValue DB_BEER_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue CP_CROk_POT_TASK_ENABLED;

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
        FD_COOK_POT_TASK_ENABLED = builder.define("FdCookPotTaskEnabled", true);
        builder.comment("This can make the fd cutting board task enabled or not.");
        FD_CUTTING_BOARD_TASK_ENABLED = builder.define("FdCuttingBoardTaskEnabled", true);
        builder.comment("This can make the cd cuisine skillet task enabled or not.");
        CD_CUISINE_SKILLET_TASK_ENABLED = builder.define("CdCuisineSkilletTaskEnabled", true);
        builder.comment("This can make the md cook pot task enabled or not.");
        MD_COOK_POT_TASK_ENABLED = builder.define("MdCookPotTaskEnabled", true);
        builder.comment("This can make the bnc key task enabled or not.");
        BNC_KEY_TASK_ENABLED = builder.define("BncKeyTaskEnabled", true);
        builder.comment("This can make the fr kettle task enabled or not.");
        FR_KETTLE_TASK_ENABLED = builder.define("FrKettleTaskEnabled", true);
        builder.comment("This can make the yhc moka task enabled or not.");
        YHC_MOKA_TASK_ENABLED = builder.define("YhcMokaTaskEnabled", true);
        builder.comment("This can make the yhc tea kettle task enabled or not.");
        YHC_TEA_KETTLE_TASK_ENABLED = builder.define("YhcTeaKettleTaskEnabled", true);
        builder.comment("This can make the yhc tea kettle task enabled or not.");
        YHC_DRYING_RACK_TASK_ENABLED = builder.define("YhcDryingRackTaskEnabled", true);
        builder.comment("This can make the yhc fermentation tank task enabled or not.");
        YHC_FERMENTATION_TANK_TASK_ENABLED = builder.define("YhcFermentationTaskEnabled", true);

        builder.comment("This can make the ck crock pot task enabled or not.");
        CP_CROk_POT_TASK_ENABLED = builder.define("CkCrockPotTaskEnabled", true);
        builder.comment("This can make the db beer task enabled or not.");
        DB_BEER_TASK_ENABLED = builder.define("DbBeerTaskEnabled", true);
        builder.comment("This can make the kc brew barrel task enabled or not.");
        KK_BREW_BARREL = builder.define("KkBrewBarrelTaskEnabled", true);
        builder.comment("This can make the kc air compressor task enabled or not.");
        KK_AIR_COMPRESSOR = builder.define("KkAirCompressorTaskEnabled", true);

        builder.pop();
    }
}
