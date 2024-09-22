package com.github.catbert.tlma.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskConfig {

    public static ForgeConfigSpec.ConfigValue<List<List<String>>> MELON_STEM_LIST;
    public static ForgeConfigSpec.ConfigValue<Integer> COOK_SELECTED_RECIPES;
    public static ForgeConfigSpec.ConfigValue<Integer> SEARCHY_OFFSET;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("Task");

        builder.comment("These entries configure the melon stem list.", "rule: [melon_item_id, attached_melon_stem_block_id]", "Eg: [\"minecraft:melon\", \"minecraft:attached_melon_stem\"]");
        MELON_STEM_LIST = builder.define("MelonStemList", getmelonStemList());

        builder.comment("These can configure the cook selected recipes max size.");
        COOK_SELECTED_RECIPES = builder.define("CookSelectedRecipes", 10);

        builder.comment("These can configure the fruit task searchy offset.");
        SEARCHY_OFFSET = builder.define("SearchyOffset", 4);

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
