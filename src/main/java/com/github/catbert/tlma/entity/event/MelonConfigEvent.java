package com.github.catbert.tlma.entity.event;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.config.subconfig.TaskConfig;
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

import static com.github.catbert.tlma.util.BlockUtil.getId;

@Mod.EventBusSubscriber(modid = TLMAddon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MelonConfigEvent {
    public static final Map<String, String> MELON_STEM_MAP = new HashMap<>();
    private static final String CONFIG_NAME = TLMAddon.MOD_ID + "-common.toml";

    @SubscribeEvent
    public static void onEvent(ModConfigEvent.Loading event) {
        String fileName = event.getConfig().getFileName();
        if (CONFIG_NAME.equals(fileName)) {
            handleConfig();
        }
    }

    public static void handleConfig() {
        MELON_STEM_MAP.clear();
        handleMelonStemList(TaskConfig.MELON_STEM_LIST.get(), MELON_STEM_MAP);
        handleMelonAndStemList(TaskConfig.MELON_AND_STEM_LIST.get(), MELON_STEM_MAP);
    }

    private static void handleMelonStemList(List<String> config, Map<String, String> output) {
        for (String s : config) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
            if (block == null) continue;

            if (block instanceof AttachedStemBlock attachedStemBlock) {
                output.put(getId(attachedStemBlock.fruit), getId(attachedStemBlock));
            }
        }
    }

    private static void handleMelonAndStemList(List<List<String>> config, Map<String, String> output) {
        for (List<String> strings : config) {
            if (strings.size() < 2) continue;

            String melonId = strings.get(0);
            String stemId = strings.get(1);

            Block melonBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(melonId));
            Block stemBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(stemId));
            if (melonBlock == null || stemBlock == null) continue;

            output.put(melonId, stemId);
        }
    }
}
