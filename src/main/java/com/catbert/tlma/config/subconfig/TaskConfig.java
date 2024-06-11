package com.catbert.tlma.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class TaskConfig {

    public static ForgeConfigSpec.BooleanValue BERRY_FARM_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue FRUIT_FARM_TASK_ENABLED;
    public static ForgeConfigSpec.BooleanValue SERENESEASONS_FARM_TASK_ENABLED;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("task");

        builder.comment("This can make the berry farm task enabled or not.");
        BERRY_FARM_TASK_ENABLED = builder.define("BerryTaskEnabled", true);
        builder.comment("This can make the fruit farm task enabled or not.");
        FRUIT_FARM_TASK_ENABLED = builder.define("FruitTaskEnabled", true);
        builder.comment("This can make the sereneseasons farm task enabled or not.");
        SERENESEASONS_FARM_TASK_ENABLED = builder.define("SereneSeasonsTaskEnabled", true);

        builder.pop();
    }
}
