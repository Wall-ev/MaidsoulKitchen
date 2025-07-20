package com.github.wallev.maidsoulkitchen.event;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.config.subconfig.TaskConfig;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MelonConfigEvent {
    public static final Map<Block, Block> MELON_STEM_MAP = new HashMap<>();
    private static final String CONFIG_NAME = MaidsoulKitchen.MOD_ID + "-common.toml";

    @SubscribeEvent
    public static void onEvent(ModConfigEvent.Loading event) {
        String fileName = event.getConfig().getFileName();
        if (CONFIG_NAME.equals(fileName)) {
            handleConfig();
        }
    }

    public static void handleConfig() {
        MELON_STEM_MAP.clear();
        handleMelonStemList(MELON_STEM_MAP);
        handleMelonAndStemList(TaskConfig.MELON_AND_STEM_LIST.get(), MELON_STEM_MAP);
    }

    private static void handleMelonStemList(Map<Block, Block> output) {
        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (block instanceof AttachedStemBlock attachedStemBlock) {
                output.put(attachedStemBlock.fruit, attachedStemBlock);
            }
        }
    }

    private static void handleMelonAndStemList(List<List<String>> config, Map<Block, Block> output) {
        for (List<String> strings : config) {
            if (strings.size() < 2) continue;

            String melonId = strings.get(0);
            String stemId = strings.get(1);

            ResourceLocation melonLoc = VResourceLocation.tryParse(melonId);
            ResourceLocation stemLoc = VResourceLocation.tryParse(stemId);
            if (melonLoc == null || stemLoc == null) continue;

            Block melonBlock = ForgeRegistries.BLOCKS.getValue(melonLoc);
            Block stemBlock = ForgeRegistries.BLOCKS.getValue(stemLoc);
            if (melonBlock == null || stemBlock == null) continue;

            output.put(melonBlock, stemBlock);
        }
    }
}
