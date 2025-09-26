package com.github.wallev.maidsoulkitchen.config.subconfig;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskConfig {

    public static ForgeConfigSpec.ConfigValue<List<List<String>>> MELON_AND_STEM_LIST;
    public static ForgeConfigSpec.ConfigValue<List<String>> BLACK_HUB_CHEST_LIST;
    public static ForgeConfigSpec.ConfigValue<Integer> FEED_SINGLE_ANIMAL_MAX_NUMBER;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("Task");

        builder.comment("These entries configure the melon stem and melon_block item list.", "rule: [melon_block_id, attached_melon_stem_block_id]", "Eg: [\"minecraft:melon\", \"minecraft:attached_melon_stem\"]");
        MELON_AND_STEM_LIST = builder.define("MelonAndStemList", getmelonAndStemList());

        builder.comment("A list of blocks that the Culinary Hub cannot interact with.");
        BLACK_HUB_CHEST_LIST = builder.define("BlackHubChestList", Lists.newArrayList());

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
