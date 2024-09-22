package com.github.catbert.tlma.compat.cloth;

import com.github.catbert.tlma.config.subconfig.RenderConfig;
import com.github.catbert.tlma.config.subconfig.TaskConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuIntegration {
    public static ConfigBuilder getConfigBuilder() {
        ConfigBuilder root = ConfigBuilder.create().setTitle(Component.literal("Touhou Little Maid Addon"));
        root.setGlobalized(true);
        root.setGlobalizedExpanded(false);
        return getConfigBuilder(root, false);
    }

    public static ConfigBuilder getConfigBuilder(ConfigBuilder root, boolean tlmEntry) {
        ConfigEntryBuilder entryBuilder = root.entryBuilder();
        taskConfig(root, entryBuilder, tlmEntry);
        renderConfig(root, entryBuilder, tlmEntry);
        return root;
    }

    public static void taskConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder, boolean tlmEntry) {
        MutableComponent entryTitle = Component.translatable("config.touhou_little_maid_addon.task.name");
        if (tlmEntry) {
            entryTitle.append(Component.literal("[Addon: Farm and Cook]").withStyle(ChatFormatting.YELLOW));
        }
        ConfigCategory task = root.getOrCreateCategory(entryTitle);

//        task.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.task.berry_farm_task.name"), TaskConfig.BERRY_FARM_TASK_ENABLED.get())
//                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.chair.berry_farm_task.desc"))
//                .setSaveConsumer(TaskConfig.BERRY_FARM_TASK_ENABLED::set).build());
//
//        task.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.task.fruit_farm_task.name"), TaskConfig.FRUIT_FARM_TASK_ENABLED.get())
//                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.chair.fruit_farm_task.desc"))
//                .setSaveConsumer(TaskConfig.FRUIT_FARM_TASK_ENABLED::set).build());
//
//        task.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.task.serene_seasons_farm_task.name"), TaskConfig.SERENESEASONS_FARM_TASK_ENABLED.get())
//                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.task.serene_seasons_farm_task.desc"))
//                .setSaveConsumer(TaskConfig.SERENESEASONS_FARM_TASK_ENABLED::set).build());

        task.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.task.enable_cook_task_enable_condition.name"), TaskConfig.COOK_TASK_ENABLE_CONDITION.get())
                .setDefaultValue(TaskConfig.COOK_TASK_ENABLE_CONDITION.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid_addon.task.enable_cook_task_enable_condition.desc"))
                .setSaveConsumer(TaskConfig.COOK_TASK_ENABLE_CONDITION::set).build());

        task.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid_addon.melon_stem_list.name"), TaskConfig.MELON_STEM_LIST.get().stream().map(s -> s.get(0) + "," + s.get(1)).toList())
                .setDefaultValue(TaskConfig.MELON_STEM_LIST.getDefault().stream().map(s -> s.get(0) + "," + s.get(1)).toList())
                .setTooltip(Component.translatable("config.touhou_little_maid_addon.melon_stem_list.desc"))
                .setSaveConsumer(l -> {
                    List<List<String>> melonStemList = new ArrayList<>();
                    for (String s : l) {
                        String[] split = s.split(",");
                        if (split.length < 2) continue;
                        melonStemList.add(Arrays.asList(split[0], split[1]));
                    }
                    TaskConfig.MELON_STEM_LIST.set(melonStemList);
                }).build());

        task.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid_addon.cook_selected_recipes.name"), TaskConfig.COOK_SELECTED_RECIPES.get())
                .setDefaultValue(TaskConfig.COOK_SELECTED_RECIPES.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid_addon.cook_selected_recipes.desc"))
                .setSaveConsumer(TaskConfig.COOK_SELECTED_RECIPES::set).build());

        task.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid_addon.fruit_search_yoffset.name"), TaskConfig.SEARCHY_OFFSET.get())
                .setDefaultValue(TaskConfig.SEARCHY_OFFSET.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid_addon.fruit_search_yoffset.desc"))
                .setSaveConsumer(TaskConfig.SEARCHY_OFFSET::set).build());
    }

    public static void renderConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder, boolean tlmEntry) {
        MutableComponent entryTitle = Component.translatable("config.touhou_little_maid_addon.render.name");
        if (tlmEntry) {
            entryTitle.append(Component.literal("[Addon: Farm and Cook]").withStyle(ChatFormatting.YELLOW));
        }
        ConfigCategory task = root.getOrCreateCategory(entryTitle);

        task.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.render.ld_banner_render.name"), RenderConfig.LD_BANNER_RENDER_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.render.ld_banner_render.desc"))
                .setSaveConsumer(RenderConfig.LD_BANNER_RENDER_ENABLED::set).build());

    }

    public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> getConfigBuilder().setParentScreen(parent).build()));
    }
}
