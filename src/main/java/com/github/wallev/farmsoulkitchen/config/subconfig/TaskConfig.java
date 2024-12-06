package com.github.wallev.farmsoulkitchen.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.wallev.farmsoulkitchen.util.BlockUtil.getId;

public class TaskConfig {

    public static ForgeConfigSpec.ConfigValue<List<List<String>>> MELON_AND_STEM_LIST;
    public static ForgeConfigSpec.ConfigValue<Integer> COOK_SELECTED_RECIPES;
    public static ForgeConfigSpec.ConfigValue<Integer> FEED_SINGLE_ANIMAL_MAX_NUMBER;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("Task");

        builder.comment("These entries configure the melon stem and melon_block item list.", "rule: [melon_block_id, attached_melon_stem_block_id]", "Eg: [\"minecraft:melon\", \"minecraft:attached_melon_stem\"]");
        MELON_AND_STEM_LIST = builder.define("MelonAndStemList", getmelonAndStemList());

        builder.comment("These can configure the cook selected recipes max size.");
        COOK_SELECTED_RECIPES = builder.define("CookSelectedRecipes", 30);

        builder.comment("The max number of the different type animal around when the maid breeds animals");
        FEED_SINGLE_ANIMAL_MAX_NUMBER = builder.defineInRange("FeedSingleAnimalMaxNumber", 20, 6, 65536);

        builder.pop();
    }

    private static List<List<String>> getmelonAndStemList() {
        List<List<String>> melonStemList = new ArrayList<>();
        melonStemList.add(Arrays.asList("simplefarming:cantaloupe", "simplefarming:attached_cantaloupe_stem"));
        melonStemList.add(Arrays.asList("simplefarming:honeydew", "simplefarming:attached_honeydew_stem"));
        melonStemList.add(Arrays.asList("simplefarming:squash", "simplefarming:attached_squash_stem"));
        return melonStemList;
    }
}
