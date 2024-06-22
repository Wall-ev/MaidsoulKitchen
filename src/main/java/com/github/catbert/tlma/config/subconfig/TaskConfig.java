package com.github.catbert.tlma.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskConfig {

//    public static ForgeConfigSpec.BooleanValue BERRY_FARM_TASK_ENABLED;
//    public static ForgeConfigSpec.BooleanValue FRUIT_FARM_TASK_ENABLED;
//    public static ForgeConfigSpec.BooleanValue SERENESEASONS_FARM_TASK_ENABLED;

    public static ForgeConfigSpec.ConfigValue<List<List<String>>> MELON_STEM_LIST;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("task");

//        builder.comment("This can make the berry farm task enabled or not.");
//        BERRY_FARM_TASK_ENABLED = builder.define("BerryTaskEnabled", true);
//        builder.comment("This can make the fruit farm task enabled or not.");
//        FRUIT_FARM_TASK_ENABLED = builder.define("FruitTaskEnabled", true);
//        builder.comment("This can make the sereneseasons farm task enabled or not.");
//        SERENESEASONS_FARM_TASK_ENABLED = builder.define("SereneSeasonsTaskEnabled", true);

        builder.comment("These entries configure the melon stem list.", "rule: [melon_block_item_id, attached_melon_stem_block_id]", "Eg: [\"minecraft:melon_block\", \"minecraft:attached_melon_stem\"]");
        MELON_STEM_LIST = builder.define("MelonStemList", getmelonStemList());

        builder.pop();
    }

    private static List<List<String>> getmelonStemList() {
        List<List<String>> melonStemList = new ArrayList<>();
        melonStemList.add(Arrays.asList("simplefarming:cantaloupe_block", "simplefarming:attached_cantaloupe_stem"));
        melonStemList.add(Arrays.asList("simplefarming:honeydew_block", "simplefarming:attached_honeydew_stem"));
        melonStemList.add(Arrays.asList("simplefarming:squash_block", "simplefarming:attached_squash_stem"));
        return melonStemList;
    }
}
