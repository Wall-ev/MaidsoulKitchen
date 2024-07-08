package com.github.catbert.tlma.event;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.config.subconfig.TaskConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TaskMelonEvent {
    private static final String CONFIG_NAME = TLMAddon.MOD_ID + "-common.toml";

    public static final Map<Block, Pair<Block, Item>> MELON_STEM_MAP = new HashMap<>();
    @SubscribeEvent
    public static void onEvent(ModConfigEvent.Loading event) {
        String fileName = event.getConfig().getFileName();
        if (CONFIG_NAME.equals(fileName)) {
            handleConfig(TaskConfig.MELON_STEM_LIST.get(), MELON_STEM_MAP);
        }
    }

    public static void handleConfig(List<List<String>> config, Map<Block, Pair<Block, Item>> output) {
        output.clear();
        for (List<String> strings : config) {
            if (strings.size() < 2) continue;

            String melonId = strings.get(0);
            String stemId = strings.get(1);

            Block stemBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stemId));
            if (stemBlock == null) continue;

            if (ForgeRegistries.ITEMS.getValue(new ResourceLocation(melonId)) instanceof BlockItem blockItem) {
                output.put(blockItem.getBlock(), Pair.of(stemBlock, blockItem.asItem()));
            }
        }
    }


}
