package com.github.catbert.tlma.compat.cloth;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.config.subconfig.RegisterConfig;
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
    private static final String TIP = "[Addon: Farm and Cook]";
    public static ConfigBuilder getConfigBuilder() {
        ConfigBuilder root = ConfigBuilder.create().setTitle(Component.literal("Touhou Little Maid Addon"));
        root.setGlobalized(true);
        root.setGlobalizedExpanded(false);
        return getConfigBuilder(root, false);
    }

    public static ConfigBuilder getConfigBuilder(ConfigBuilder root, boolean tlmEntry) {
        addConfig(root, root.entryBuilder(), tlmEntry);
        return root;
    }

    public static void addConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder, boolean tlmEntry) {
        taskConfig(root, entryBuilder, tlmEntry);
        renderConfig(root, entryBuilder, tlmEntry);
        registerConfig(root, entryBuilder, tlmEntry);
    }

    private static void registerConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder, boolean tlmEntry) {
        MutableComponent entryTitle = Component.translatable("config.touhou_little_maid_addon.register");
        MutableComponent addition = Component.empty();
        if (tlmEntry) {
            entryTitle.append(Component.literal(TIP).withStyle(ChatFormatting.YELLOW));
            addition.append(Component.literal("\n" + TIP).withStyle(ChatFormatting.BLUE))
                    .append(Component.literal("\nModId: " + TLMAddon.MOD_ID).withStyle(ChatFormatting.DARK_GRAY));
        }
        ConfigCategory register = root.getOrCreateCategory(entryTitle);

        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.berry_farm_task"), RegisterConfig.BERRY_FARM_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.berry_farm_task.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.BERRY_FARM_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.fruit_farm_task"), RegisterConfig.FRUIT_FARM_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.fruit_farm_task.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.FRUIT_FARM_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.compat_melon_farm_task"), RegisterConfig.FRUIT_FARM_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.compat_melon_farm_task.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.FRUIT_FARM_TASK_ENABLED::set).build());

        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.serene_seasons_farm_task"), RegisterConfig.SERENESEASONS_FARM_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.serene_seasons_farm_task.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.SERENESEASONS_FARM_TASK_ENABLED::set).build());

        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.feed_and_drink_task"), RegisterConfig.FEED_AND_DRINK_OWNER_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.feed_and_drink_task.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.FEED_AND_DRINK_OWNER_TASK_ENABLED::set).build());

        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.furnace_task"), RegisterConfig.FURNACE_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.furnace_task.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.FURNACE_TASK_ENABLED::set).build());

        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.fd_cook_pot"), RegisterConfig.FD_COOK_POT_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.fd_cook_pot.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.FD_COOK_POT_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.md_cook_pot"), RegisterConfig.MD_COOK_POT_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.md_cook_pot.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.MD_COOK_POT_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.bnc_key"), RegisterConfig.BNC_KEY_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.bnc_key.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.BNC_KEY_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.yhc_moka"), RegisterConfig.YHC_MOKA_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.yhc_moka.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.YHC_MOKA_TASK_ENABLED::set).build());

        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.db_beer"), RegisterConfig.DB_BEER_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.db_beer.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.DB_BEER_TASK_ENABLED::set).build());

        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.dbk_cooking_pot"), RegisterConfig.DBK_COOKING_POT_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.dbk_cooking_pot.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.DBK_COOKING_POT_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.dbk_stove"), RegisterConfig.DBK_STOVE_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.dbk_stove.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.DBK_STOVE_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.dbp_mini_fridge"), RegisterConfig.DBP_MINE_FRIDGE_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.dbp_mini_fridge.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.DBP_MINE_FRIDGE_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.dbp_tiki_bar"), RegisterConfig.DBP_TIKI_BAR_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.dbp_tiki_bar.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.DBP_TIKI_BAR_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.dcl_cooking_pan"), RegisterConfig.DCL_COOKING_PAN_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.dcl_cooking_pan.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.DCL_COOKING_PAN_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.dcl_cooking_pot"), RegisterConfig.DCL_COOKING_POT_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.dcl_cooking_pot.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.DCL_COOKING_POT_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.dhb_cauldron"), RegisterConfig.DHB_CAULDRON_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.dhb_cauldron.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.DHB_CAULDRON_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.dhb_tea_kettle"), RegisterConfig.DHB_TEA_KETTLE_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.dhb_tea_kettle.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.DHB_TEA_KETTLE_TASK_ENABLED::set).build());
        register.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.register.fermentation_barrel"), RegisterConfig.FERMENTATION_BARREL_TASK_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.register.fermentation_barrel.tooltip"),
                        Component.translatable("config.touhou_little_maid_addon.register.restart_warn.tooltip"), addition)
                .setSaveConsumer(RegisterConfig.FERMENTATION_BARREL_TASK_ENABLED::set).build());
    }

    private static void taskConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder, boolean tlmEntry) {
        MutableComponent entryTitle = Component.translatable("config.touhou_little_maid_addon.task");
        MutableComponent addition = Component.empty();
        if (tlmEntry) {
            entryTitle.append(Component.literal(TIP).withStyle(ChatFormatting.YELLOW));
            addition.append(Component.literal("\n" + TIP).withStyle(ChatFormatting.BLUE))
                    .append(Component.literal("\nModId: " + TLMAddon.MOD_ID).withStyle(ChatFormatting.DARK_GRAY));
        }
        ConfigCategory task = root.getOrCreateCategory(entryTitle);

        task.addEntry(entryBuilder.startStrList(Component.translatable("config.touhou_little_maid_addon.task.melon_stem_list"), TaskConfig.MELON_STEM_LIST.get().stream().map(s -> s.get(0) + "," + s.get(1)).toList())
                .setDefaultValue(TaskConfig.MELON_STEM_LIST.getDefault().stream().map(s -> s.get(0) + "," + s.get(1)).toList())
                .setTooltip(Component.translatable("config.touhou_little_maid_addon.task.melon_stem_list.tooltip"), addition)
                .setSaveConsumer(l -> {
                    List<List<String>> melonStemList = new ArrayList<>();
                    for (String s : l) {
                        String[] split = s.split(",");
                        if (split.length < 2) continue;
                        melonStemList.add(Arrays.asList(split[0], split[1]));
                    }
                    TaskConfig.MELON_STEM_LIST.set(melonStemList);
                }).build());

        task.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid_addon.task.cook_selected_recipes"), TaskConfig.COOK_SELECTED_RECIPES.get())
                .setDefaultValue(TaskConfig.COOK_SELECTED_RECIPES.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid_addon.task.cook_selected_recipes.tooltip"), addition)
                .setSaveConsumer(TaskConfig.COOK_SELECTED_RECIPES::set).build());

        task.addEntry(entryBuilder.startIntField(Component.translatable("config.touhou_little_maid_addon.task.fruit_search_yoffset"), TaskConfig.SEARCHY_OFFSET.get())
                .setDefaultValue(TaskConfig.SEARCHY_OFFSET.getDefault())
                .setTooltip(Component.translatable("config.touhou_little_maid_addon.task.fruit_search_yoffset.tooltip"), addition)
                .setSaveConsumer(TaskConfig.SEARCHY_OFFSET::set).build());
    }

    private static void renderConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder, boolean tlmEntry) {
        MutableComponent entryTitle = Component.translatable("config.touhou_little_maid_addon.render");
        MutableComponent addition = Component.empty();
        if (tlmEntry) {
            entryTitle.append(Component.literal(TIP).withStyle(ChatFormatting.YELLOW));
            addition.append(Component.literal("\n" + TIP).withStyle(ChatFormatting.BLUE))
                    .append(Component.literal("\nModId: " + TLMAddon.MOD_ID).withStyle(ChatFormatting.DARK_GRAY));
        }
        ConfigCategory render = root.getOrCreateCategory(entryTitle);

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.touhou_little_maid_addon.render.ld_banner_render"), RenderConfig.LD_BANNER_RENDER_ENABLED.get())
                .setDefaultValue(true).setTooltip(Component.translatable("config.touhou_little_maid_addon.render.ld_banner_render.tooltip"), addition)
                .setSaveConsumer(RenderConfig.LD_BANNER_RENDER_ENABLED::set).build());

    }

    public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> getConfigBuilder().setParentScreen(parent).build()));
    }
}
